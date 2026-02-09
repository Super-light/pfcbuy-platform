package com.pfcbuy.payment.dto;

import lombok.Data;

/**
 * Stripe Webhook 事件请求
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
public class StripeWebhookRequest {

    /**
     * Stripe 签名头
     */
    private String stripeSignature;

    /**
     * Webhook 事件数据（原始JSON）
     */
    private String payload;
}
