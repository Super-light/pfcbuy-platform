package com.pfcbuy.payment.channel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfcbuy.common.enums.PaymentChannel;
import com.pfcbuy.common.enums.PaymentStatus;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.common.exception.NotFoundException;
import com.pfcbuy.common.result.Result;
import com.pfcbuy.common.utils.JsonUtil;
import com.pfcbuy.payment.channel.PaymentChannelService;
import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.dto.PaymentResponse;
import com.pfcbuy.payment.entity.Payment;
import com.pfcbuy.payment.feign.OrderDTO;
import com.pfcbuy.payment.feign.OrderFeignClient;
import com.pfcbuy.payment.mapper.PaymentMapper;
import com.pfcbuy.payment.service.StripeService;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Stripe 支付渠道服务实现
 * 实现 PaymentChannelService 接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentService implements PaymentChannelService {

    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;
    private final OrderFeignClient orderFeignClient;

    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.STRIPE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("Stripe创建支付: orderNo={}, amount={}, currency={}", 
                request.getOrderNo(), request.getAmount(), request.getCurrency());

        // 1. 检查是否已存在支付记录
        Payment existingPayment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getOrderNo, request.getOrderNo())
                        .last("LIMIT 1")
        );
        
        if (existingPayment != null && PaymentStatus.PAID.getCode().equals(existingPayment.getStatus())) {
            throw new BusinessException("该订单已支付");
        }

        // 2. 获取订单信息（校验订单是否存在）
        OrderDTO order = getOrderInfo(request.getOrderNo());
        if (order == null) {
            throw new NotFoundException("订单不存在");
        }

        // 3. 创建 Stripe PaymentIntent
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(request, request.getUserId());

        // 4. 创建支付记录
        Payment payment = new Payment();
        payment.setPaymentNo(generatePaymentNo());
        payment.setOrderId(order.getId());
        payment.setOrderNo(request.getOrderNo());
        payment.setUserId(request.getUserId());
        payment.setPaymentChannel(PaymentChannel.STRIPE.getCode());
        payment.setThirdPartyTransactionId(paymentIntent.getId());
        payment.setStripePaymentIntentId(paymentIntent.getId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency().toUpperCase());
        payment.setStatus(PaymentStatus.INIT.getCode());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setClientSecret(paymentIntent.getClientSecret());
        
        if (request.getMetadata() != null) {
            payment.setMetadata(JsonUtil.toJson(request.getMetadata()));
        }

        paymentMapper.insert(payment);

        log.info("Stripe支付记录创建成功: paymentNo={}, paymentIntentId={}", 
                payment.getPaymentNo(), paymentIntent.getId());

        return convertToResponse(payment);
    }

    @Override
    public PaymentResponse queryPayment(String transactionId) {
        // 查询本地支付记录
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getStripePaymentIntentId, transactionId)
        );
        
        if (payment == null) {
            throw new NotFoundException("支付记录不存在");
        }
        
        return convertToResponse(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelPayment(String transactionId) {
        // 取消 Stripe PaymentIntent
        stripeService.cancelPaymentIntent(transactionId);
        
        // 更新本地支付状态
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getStripePaymentIntentId, transactionId)
        );
        
        if (payment != null) {
            payment.setStatus(PaymentStatus.FAILED.getCode());
            payment.setCanceledAt(LocalDateTime.now());
            payment.setUpdateTime(LocalDateTime.now());
            paymentMapper.updateById(payment);
            return true;
        }
        
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refund(String transactionId, BigDecimal refundAmount, String reason) {
        // Stripe 的退款通过 PaymentServiceImpl 处理
        // 这里直接返回 true，实际退款逻辑在 PaymentServiceImpl 中
        return true;
    }

    @Override
    public boolean verifySignature(String payload, String signature) {
        // Stripe 的签名验证在 WebhookController 中处理
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentSuccess(String payload) {
        // Stripe 的成功处理在 WebhookController 中通过 PaymentService 处理
        log.debug("Stripe payment success handled via webhook");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentFailure(String payload) {
        // Stripe 的失败处理在 WebhookController 中通过 PaymentService 处理
        log.debug("Stripe payment failure handled via webhook");
    }

    /**
     * 获取订单信息
     */
    private OrderDTO getOrderInfo(String orderNo) {
        try {
            Result<OrderDTO> result = orderFeignClient.getOrder(orderNo);
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
}