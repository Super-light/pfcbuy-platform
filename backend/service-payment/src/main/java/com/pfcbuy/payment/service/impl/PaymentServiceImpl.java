package com.pfcbuy.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.pfcbuy.common.enums.PaymentChannel;
import com.pfcbuy.common.enums.PaymentStatus;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.common.exception.NotFoundException;
import com.pfcbuy.common.utils.JsonUtil;
import com.pfcbuy.payment.channel.PaymentChannelFactory;
import com.pfcbuy.payment.channel.PaymentChannelService;
import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.dto.PaymentResponse;
import com.pfcbuy.payment.entity.Payment;
import com.pfcbuy.payment.feign.OrderDTO;
import com.pfcbuy.payment.feign.OrderFeignClient;
import com.pfcbuy.payment.mapper.PaymentMapper;
import com.pfcbuy.payment.service.PaymentService;
import com.pfcbuy.payment.service.StripeService;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 支付服务实现类
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentChannelFactory channelFactory;
    private final StripeService stripeService; // 保留以支持Stripe特定功能
    private final OrderFeignClient orderFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse createPayment(CreatePaymentRequest request, Long userId) {
        log.info("创建支付: orderNo={}, amount={}, currency={}, channel={}",
                request.getOrderNo(), request.getAmount(), request.getCurrency(), request.getPaymentChannel());

        // 1. 设置用户ID
        request.setUserId(userId);

        // 2. 获取支付渠道服务
        PaymentChannel channel = PaymentChannel.fromCode(request.getPaymentChannel());
        PaymentChannelService channelService = channelFactory.getService(channel);

        // 3. 调用渠道服务创建支付
        return channelService.createPayment(request);
    }

    @Override
    public PaymentResponse getPaymentByOrderNo(String orderNo) {
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getOrderNo, orderNo)
                        .last("LIMIT 1")
        );
        if (payment == null) {
            throw new NotFoundException("支付记录不存在");
        }
        return convertToResponse(payment);
    }

    @Override
    public Payment getPaymentById(Long paymentId) {
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null) {
            throw new NotFoundException("支付记录不存在");
        }
        return payment;
    }

    @Override
    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        List<Payment> payments = paymentMapper.selectList(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getUserId, userId)
                        .orderByDesc(Payment::getCreateTime)
        );
        return payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentSucceeded(String paymentIntentId, String chargeId) {
        log.info("处理支付成功: paymentIntentId={}, chargeId={}", paymentIntentId, chargeId);

        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getStripePaymentIntentId, paymentIntentId)
                        .last("LIMIT 1")
        );
        if (payment == null) {
            log.error("支付记录不存在: paymentIntentId={}", paymentIntentId);
            return;
        }

        // 更新支付状态为成功
        paymentMapper.update(null,
                new LambdaUpdateWrapper<Payment>()
                        .eq(Payment::getId, payment.getId())
                        .set(Payment::getStatus, PaymentStatus.PAID.getCode())
                        .set(Payment::getStripeChargeId, chargeId)
                        .set(Payment::getPaidAt, LocalDateTime.now())
                        .set(Payment::getUpdateTime, LocalDateTime.now())
        );

        // 通知订单服务更新订单状态
        try {
            orderFeignClient.updatePaymentStatus(payment.getOrderNo(), payment.getPaymentNo());
            log.info("已通知订单服务更新支付状态: orderNo={}", payment.getOrderNo());
        } catch (Exception e) {
            log.error("通知订单服务失败: {}", e.getMessage(), e);
            // 这里可以加入重试机制或消息队列
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentFailed(String paymentIntentId, String failureReason) {
        log.info("处理支付失败: paymentIntentId={}, reason={}", paymentIntentId, failureReason);

        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getStripePaymentIntentId, paymentIntentId)
                        .last("LIMIT 1")
        );
        if (payment == null) {
            log.error("支付记录不存在: paymentIntentId={}", paymentIntentId);
            return;
        }

        payment.setStatus(PaymentStatus.FAILED.getCode());
        payment.setFailureReason(failureReason);
        payment.setUpdateTime(LocalDateTime.now());
        paymentMapper.updateById(payment);

        log.info("支付失败记录已更新: paymentNo={}", payment.getPaymentNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);

        if (PaymentStatus.PAID.getCode().equals(payment.getStatus())) {
            throw new BusinessException("已支付成功的订单无法取消");
        }

        // 取消 Stripe PaymentIntent
        stripeService.cancelPaymentIntent(payment.getStripePaymentIntentId());

        // 更新支付状态
        payment.setStatus(PaymentStatus.FAILED.getCode());
        payment.setCanceledAt(LocalDateTime.now());
        payment.setUpdateTime(LocalDateTime.now());
        paymentMapper.updateById(payment);

        log.info("支付已取消: paymentNo={}", payment.getPaymentNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRefund(Long paymentId, BigDecimal refundAmount) {
        Payment payment = getPaymentById(paymentId);

        if (!PaymentStatus.PAID.getCode().equals(payment.getStatus())) {
            throw new BusinessException("只能对已支付成功的订单进行退款");
        }

        if (payment.getStripeChargeId() == null) {
            throw new BusinessException("缺少 Charge ID，无法退款");
        }

        // 验证退款金额
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new BusinessException("退款金额不能超过支付金额");
        }

        // 创建 Stripe 退款
        Long refundAmountInCents = convertToSmallestUnit(refundAmount, payment.getCurrency());
        stripeService.createRefund(payment.getStripeChargeId(), refundAmountInCents);

        // 更新支付记录
        payment.setStatus(PaymentStatus.REFUNDED.getCode());
        payment.setRefundAmount(refundAmount);
        payment.setRefundedAt(LocalDateTime.now());
        payment.setUpdateTime(LocalDateTime.now());
        paymentMapper.updateById(payment);

        log.info("退款成功: paymentNo={}, refundAmount={}", payment.getPaymentNo(), refundAmount);
    }

    /**
     * 获取订单信息
     */
    private OrderDTO getOrderInfo(String orderNo) {
        try {
            var result = orderFeignClient.getOrder(orderNo);
            if (result != null && result.isSuccess()) {
                return result.getData();
            }
            return null;
        } catch (Exception e) {
            log.error("获取订单信息失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 生成支付流水号
     */
    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 转换为响应对象
     */
    private PaymentResponse convertToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentNo(payment.getPaymentNo())
                .orderNo(payment.getOrderNo())
                .paymentChannel(payment.getPaymentChannel())
                .thirdPartyTransactionId(payment.getThirdPartyTransactionId())
                .stripePaymentIntentId(payment.getStripePaymentIntentId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .clientSecret(payment.getClientSecret())
                .paidAt(payment.getPaidAt())
                .createTime(payment.getCreateTime())
                .updateTime(payment.getUpdateTime())
                .build();
    }

    /**
     * 将金额转换为最小货币单位
     */
    private Long convertToSmallestUnit(BigDecimal amount, String currency) {
        currency = currency.toUpperCase();
        if ("JPY".equals(currency) || "KRW".equals(currency)) {
            return amount.longValue();
        }
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
}
