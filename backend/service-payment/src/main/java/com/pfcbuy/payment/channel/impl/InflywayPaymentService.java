package com.pfcbuy.payment.channel.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfcbuy.common.enums.PaymentChannel;
import com.pfcbuy.common.enums.PaymentStatus;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.common.utils.JsonUtil;
import com.pfcbuy.payment.channel.PaymentChannelService;
import com.pfcbuy.payment.client.InflywayHttpClient;
import com.pfcbuy.payment.config.InflywayConfig;
import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.dto.PaymentResponse;
import com.pfcbuy.payment.dto.inflyway.InflywayCreatePaymentRequest;
import com.pfcbuy.payment.dto.inflyway.InflywayNotifyRequest;
import com.pfcbuy.payment.dto.inflyway.InflywayPaymentResponse;
import com.pfcbuy.payment.entity.Payment;
import com.pfcbuy.payment.mapper.PaymentMapper;
import com.pfcbuy.payment.utils.InflywaySignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 飞来汇支付服务实现
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InflywayPaymentService implements PaymentChannelService {

    private final InflywayConfig inflywayConfig;
    private final InflywayHttpClient httpClient;
    private final PaymentMapper paymentMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.INFLYWAY;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("创建飞来汇支付: orderNo={}, amount={}, currency={}", 
                request.getOrderNo(), request.getAmount(), request.getCurrency());

        // 1. 检查货币是否支持
        if (!inflywayConfig.getSupportedCurrenciesList().contains(request.getCurrency())) {
            throw new BusinessException("不支持的货币类型: " + request.getCurrency());
        }

        // 2. 生成invoice_id（业务系统流水号）
        String invoiceId = generateInvoiceId();

        // 3. 构建飞来汇请求
        InflywayCreatePaymentRequest inflywayRequest = InflywayCreatePaymentRequest.builder()
                .merchantId(inflywayConfig.getMerchantNo())
                .invoiceId(invoiceId)
                .amount(request.getAmount().toString())
                .currency(request.getCurrency())
                .notifyUrl(inflywayConfig.getNotifyUrl())
                .returnUrl(inflywayConfig.getReturnUrl())
                .description(request.getProductName() != null ? request.getProductName() : "PfcBuy Order")
                .customerEmail(request.getCustomerEmail())
                .build();

        // 4. 生成签名（签名字段: amount + currency + invoice_id + merchant_id）
        String hash = InflywaySignUtil.createPaymentSign(
                inflywayRequest.getAmount(),
                inflywayRequest.getCurrency(),
                inflywayRequest.getInvoiceId(),
                inflywayRequest.getMerchantId(),
                inflywayConfig.getApiSecret()
        );
        inflywayRequest.setHash(hash);

        log.info("飞来汇请求: invoiceId={}, hash={}", invoiceId, hash);

        // 5. 调用飞来汇API
        String apiUrl = inflywayConfig.getApiBaseUrl() + "/paycenter/api/order/payment/create";
        InflywayPaymentResponse inflywayResponse;
        
        try {
            inflywayResponse = httpClient.postWithAuth(
                    apiUrl,
                    inflywayRequest,
                    inflywayConfig.getMerchantNo(),
                    inflywayConfig.getApiSecret(),
                    "PfcBuy/1.0",
                    InflywayPaymentResponse.class
            );
        } catch (Exception e) {
            log.error("调用飞来汇API失败: {}", e.getMessage(), e);
            throw new BusinessException("创建支付失败: " + e.getMessage());
        }

        // 6. 检查响应
        if (inflywayResponse == null || !inflywayResponse.isSuccess()) {
            String errorMsg = inflywayResponse != null ? inflywayResponse.getMessage() : "未知错误";
            log.error("飞来汇创建支付失败: {}", errorMsg);
            throw new BusinessException("创建支付失败: " + errorMsg);
        }

        // 7. 保存支付记录
        Payment payment = savePaymentRecord(request, invoiceId, inflywayResponse);

        // 8. 构建返回响应
        return buildPaymentResponse(payment, inflywayResponse);
    }

    @Override
    public PaymentResponse queryPayment(String transactionId) {
        log.info("查询飞来汇支付状态: transactionId={}", transactionId);

        // 1. 构建查询请求
        Map<String, Object> params = new HashMap<>();
        params.put("merchantNo", inflywayConfig.getMerchantNo());
        params.put("orderNo", transactionId);
        params.put("timestamp", System.currentTimeMillis());

        // 2. 生成签名 - 使用正确的签名方法
        String sign = InflywaySignUtil.generateSign(params, inflywayConfig.getApiSecret());
        params.put("sign", sign);
        params.put("signType", inflywayConfig.getSignType());

        // 3. 调用查询API
        String apiUrl = inflywayConfig.getApiBaseUrl() + "/api/v1/payment/query";
        InflywayPaymentResponse inflywayResponse = httpClient.get(apiUrl, params, InflywayPaymentResponse.class);

        // 4. 更新本地支付状态
        if (inflywayResponse != null && inflywayResponse.isSuccess()) {
            updatePaymentStatus(transactionId, inflywayResponse);
        }

        // 5. 查询本地支付记录
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getInflywayOrderNo, transactionId)
        );

        if (payment == null) {
            throw new BusinessException("支付记录不存在");
        }

        return convertToPaymentResponse(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelPayment(String transactionId) {
        log.info("取消飞来汇支付: transactionId={}", transactionId);

        // 1. 查询支付记录
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getInflywayOrderNo, transactionId)
        );

        if (payment == null) {
            throw new BusinessException("支付记录不存在");
        }

        // 2. 检查状态是否可取消
        if (!PaymentStatus.INIT.getCode().equals(payment.getStatus()) 
                && !PaymentStatus.PROCESSING.getCode().equals(payment.getStatus())) {
            throw new BusinessException("当前状态不支持取消操作");
        }

        // 3. 调用飞来汇取消API（如果有）
        // TODO: 根据飞来汇文档实现取消接口

        // 4. 更新本地状态
        payment.setStatus(PaymentStatus.FAILED.getCode());
        payment.setCanceledAt(LocalDateTime.now());
        paymentMapper.updateById(payment);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refund(String transactionId, BigDecimal refundAmount, String reason) {
        log.info("飞来汇退款: transactionId={}, refundAmount={}, reason={}", 
                transactionId, refundAmount, reason);

        // 1. 查询支付记录
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getInflywayOrderNo, transactionId)
        );

        if (payment == null) {
            throw new BusinessException("支付记录不存在");
        }

        // 2. 检查是否已支付
        if (!PaymentStatus.PAID.getCode().equals(payment.getStatus())) {
            throw new BusinessException("支付未完成，无法退款");
        }

        // 3. 检查退款金额
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new BusinessException("退款金额不能大于支付金额");
        }

        // 4. 调用飞来汇退款API
        // TODO: 根据飞来汇文档实现退款接口
        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("merchantNo", inflywayConfig.getMerchantNo());
        refundParams.put("orderNo", transactionId);
        refundParams.put("refundAmount", refundAmount);
        refundParams.put("reason", reason);
        refundParams.put("timestamp", System.currentTimeMillis());

        String sign = InflywaySignUtil.generateSign(refundParams, inflywayConfig.getApiSecret());
        refundParams.put("sign", sign);

        String apiUrl = inflywayConfig.getApiBaseUrl() + "/api/v1/payment/refund";

        InflywayPaymentResponse refundResponse = httpClient.postWithAuth(apiUrl, refundParams, inflywayConfig.getMerchantNo(), inflywayConfig.getApiSecret(), "PfcBuy/1.0", InflywayPaymentResponse.class);

        if (refundResponse == null || !refundResponse.isSuccess()) {
            throw new BusinessException("退款失败: " + (refundResponse != null ? refundResponse.getMessage() : "未知错误"));
        }

        // 5. 更新本地状态
        payment.setStatus(PaymentStatus.REFUNDED.getCode());
        payment.setRefundAmount(refundAmount);
        payment.setRefundedAt(LocalDateTime.now());
        payment.setRemark(reason);
        paymentMapper.updateById(payment);

        return true;
    }

    @Override
    public boolean verifySignature(String payload, String signature) {
        try {
            // 解析回调数据
            InflywayNotifyRequest notify = JsonUtil.fromJson(payload, InflywayNotifyRequest.class);
            
            // 转换为Map并验证签名
            Map<String, Object> params = InflywaySignUtil.objectToMap(notify);
            
            // 使用正确的验证方法
            return InflywaySignUtil.verifySign(params, signature, inflywayConfig.getApiSecret());
        } catch (Exception e) {
            log.error("验证飞来汇签名失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentSuccess(String payload) {
        log.info("处理飞来汇支付成功回调: {}", payload);

        try {
            // 1. 解析回调数据
            InflywayNotifyRequest notify = JsonUtil.fromJson(payload, InflywayNotifyRequest.class);

            // 2. 验证签名
            String calculatedHash = InflywaySignUtil.paymentResultSign(
                    notify.getAmount(),
                    notify.getCurrency(),
                    notify.getInvoiceId(),
                    notify.getMerchantId(),
                    notify.getPaymentOrderId(),
                    notify.getStatus(),
                    inflywayConfig.getApiSecret()
            );

            if (!calculatedHash.equals(notify.getHash())) {
                log.error("飞来汇回调签名验证失败: invoiceId={}", notify.getInvoiceId());
                throw new BusinessException("签名验证失败");
            }

            // 3. 查询支付记录
            Payment payment = paymentMapper.selectOne(
                    new LambdaQueryWrapper<Payment>()
                            .eq(Payment::getPaymentNo, notify.getInvoiceId())
            );

            if (payment == null) {
                log.error("支付记录不存在: invoiceId={}", notify.getInvoiceId());
                return;
            }

            // 4. 幂等性检查
            if (PaymentStatus.PAID.getCode().equals(payment.getStatus())) {
                log.info("支付已处理，跳过: paymentNo={}", payment.getPaymentNo());
                return;
            }

            // 5. 更新支付状态
            payment.setStatus(PaymentStatus.PAID.getCode());
            payment.setThirdPartyTransactionId(notify.getPaymentOrderId());
            payment.setPaidAt(LocalDateTime.now());
            paymentMapper.updateById(payment);

            log.info("飞来汇支付成功处理完成: paymentNo={}", payment.getPaymentNo());

            // 6. 通知订单服务（通过 Feign 或消息队列）
            // TODO: 调用订单服务更新订单状态

        } catch (Exception e) {
            log.error("处理飞来汇支付成功回调失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePaymentFailure(String payload) {
        log.info("处理飞来汇支付失败回调: {}", payload);

        try {
            // 1. 解析回调数据
            InflywayNotifyRequest notify = JsonUtil.fromJson(payload, InflywayNotifyRequest.class);

            // 2. 验证签名
            String calculatedHash = InflywaySignUtil.paymentResultSign(
                    notify.getAmount(),
                    notify.getCurrency(),
                    notify.getInvoiceId(),
                    notify.getMerchantId(),
                    notify.getPaymentOrderId(),
                    notify.getStatus(),
                    inflywayConfig.getApiSecret()
            );

            if (!calculatedHash.equals(notify.getHash())) {
                log.error("飞来汇回调签名验证失败: invoiceId={}", notify.getInvoiceId());
                throw new BusinessException("签名验证失败");
            }

            // 3. 查询支付记录
            Payment payment = paymentMapper.selectOne(
                    new LambdaQueryWrapper<Payment>()
                            .eq(Payment::getPaymentNo, notify.getInvoiceId())
            );

            if (payment == null) {
                log.error("支付记录不存在: invoiceId={}", notify.getInvoiceId());
                return;
            }

            // 4. 更新支付状态
            payment.setStatus(PaymentStatus.FAILED.getCode());
            payment.setFailureReason(notify.getFailureReason());
            paymentMapper.updateById(payment);

            log.info("飞来汇支付失败处理完成: paymentNo={}, reason={}", 
                    payment.getPaymentNo(), notify.getFailureReason());

        } catch (Exception e) {
            log.error("处理飞来汇支付失败回调失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 保存支付记录
     */
    private Payment savePaymentRecord(CreatePaymentRequest request, String invoiceId,
                                     InflywayPaymentResponse inflywayResponse) {
        Payment payment = new Payment();
        payment.setPaymentNo(invoiceId);  // 使用invoice_id作为商户支付号
        payment.setOrderNo(request.getOrderNo());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(PaymentStatus.INIT.getCode());
        payment.setPaymentChannel(PaymentChannel.INFLYWAY.getCode());

        // 飞来汇特有字段
        if (inflywayResponse.getData() != null) {
            payment.setThirdPartyTransactionId(inflywayResponse.getData().getPaymentOrderId());
            // 修复：使用正确的字段名 inflywayPayUrl
            payment.setInflywayPayUrl(inflywayResponse.getData().getPaymentUrl());
        }

        payment.setMetadata(request.getMetadata() != null ? JsonUtil.toJson(request.getMetadata()) : null);
        
        paymentMapper.insert(payment);
        
        return payment;
    }

    /**
     * 构建支付响应
     */
    private PaymentResponse buildPaymentResponse(Payment payment, InflywayPaymentResponse inflywayResponse) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentNo(payment.getPaymentNo())
                .orderNo(payment.getOrderNo())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .paymentChannel(payment.getPaymentChannel())
                // 修复：使用正确的字段名 payUrl
                .payUrl(inflywayResponse.getData() != null ? inflywayResponse.getData().getPaymentUrl() : null)
                .clientSecret(null)  // 飞来汇不需要clientSecret
                .createTime(payment.getCreateTime())
                .build();
    }

    /**
     * 更新支付状态
     */
    private void updatePaymentStatus(String orderNo, InflywayPaymentResponse inflywayResponse) {
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getInflywayOrderNo, orderNo)
        );

        if (payment != null && inflywayResponse.isPaid()) {
            payment.setStatus(PaymentStatus.PAID.getCode());
            payment.setInflywayTradeNo(inflywayResponse.getData().getTradeNo());
            payment.setPaidAt(parseDateTime(inflywayResponse.getData().getPayTime()));
            paymentMapper.updateById(payment);
        }
    }

    /**
     * 转换为支付响应
     */
    private PaymentResponse convertToPaymentResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setPaymentNo(payment.getPaymentNo());
        response.setOrderNo(payment.getOrderNo());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus());
        response.setPaymentChannel(payment.getPaymentChannel());
        response.setPayUrl(payment.getInflywayPayUrl());
        response.setCreateTime(payment.getCreateTime());
        response.setPaidAt(payment.getPaidAt());
        
        return response;
    }

    /**
     * 生成invoice_id（业务系统流水号）
     */
    private String generateInvoiceId() {
        // 格式: PFC + 时间戳 + 随机数，长度不超过50
        return "PFC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 解析日期时间字符串
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            log.warn("解析日期时间失败: {}", dateTimeStr, e);
            return null;
        }
    }
}