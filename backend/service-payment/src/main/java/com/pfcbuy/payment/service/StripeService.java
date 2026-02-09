package com.pfcbuy.payment.service;

import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

/**
 * Stripe 服务接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
public interface StripeService {

    /**
     * 创建 PaymentIntent
     *
     * @param request 支付请求
     * @param userId  用户ID
     * @return PaymentIntent
     */
    PaymentIntent createPaymentIntent(CreatePaymentRequest request, Long userId);

    /**
     * 获取 PaymentIntent
     *
     * @param paymentIntentId PaymentIntent ID
     * @return PaymentIntent
     */
    PaymentIntent getPaymentIntent(String paymentIntentId);

    /**
     * 取消 PaymentIntent
     *
     * @param paymentIntentId PaymentIntent ID
     * @return PaymentIntent
     */
    PaymentIntent cancelPaymentIntent(String paymentIntentId);

    /**
     * 创建退款
     *
     * @param chargeId     Charge ID
     * @param refundAmount 退款金额（单位：分）
     * @return Refund
     */
    Refund createRefund(String chargeId, Long refundAmount);

    /**
     * 验证 Webhook 签名
     *
     * @param payload   请求体
     * @param signature Stripe 签名
     * @return Event
     */
    Event constructWebhookEvent(String payload, String signature);
}
