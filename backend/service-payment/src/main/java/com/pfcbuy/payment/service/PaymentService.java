package com.pfcbuy.payment.service;

import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.dto.PaymentResponse;
import com.pfcbuy.payment.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付服务接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
public interface PaymentService {

    /**
     * 创建支付
     *
     * @param request 支付请求
     * @param userId  用户ID
     * @return 支付响应
     */
    PaymentResponse createPayment(CreatePaymentRequest request, Long userId);

    /**
     * 根据订单号获取支付信息
     *
     * @param orderNo 订单号
     * @return 支付信息
     */
    PaymentResponse getPaymentByOrderNo(String orderNo);

    /**
     * 根据支付ID获取支付信息
     *
     * @param paymentId 支付ID
     * @return 支付信息
     */
    Payment getPaymentById(Long paymentId);

    /**
     * 根据用户ID获取支付列表
     *
     * @param userId 用户ID
     * @return 支付列表
     */
    List<PaymentResponse> getPaymentsByUserId(Long userId);

    /**
     * 处理支付成功
     *
     * @param paymentIntentId Stripe PaymentIntent ID
     * @param chargeId        Stripe Charge ID
     */
    void handlePaymentSucceeded(String paymentIntentId, String chargeId);

    /**
     * 处理支付失败
     *
     * @param paymentIntentId Stripe PaymentIntent ID
     * @param failureReason   失败原因
     */
    void handlePaymentFailed(String paymentIntentId, String failureReason);

    /**
     * 取消支付
     *
     * @param paymentId 支付ID
     */
    void cancelPayment(Long paymentId);

    /**
     * 创建退款
     *
     * @param paymentId    支付ID
     * @param refundAmount 退款金额
     */
    void createRefund(Long paymentId, BigDecimal refundAmount);
}
