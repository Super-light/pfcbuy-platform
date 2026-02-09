package com.pfcbuy.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付响应
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 支付流水号
     */
    private String paymentNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付渠道（STRIPE/INFLYWAY）
     */
    private String paymentChannel;

    /**
     * Stripe PaymentIntent ID
     */
    private String stripePaymentIntentId;

    /**
     * 第三方交易ID
     */
    private String thirdPartyTransactionId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付链接（用于跳转第三方支付页面）
     */
    private String payUrl;

    /**
     * 二维码链接
     */
    private String qrCodeUrl;

    /**
     * 客户端密钥（前端用于确认支付）
     */
    private String clientSecret;

    /**
     * 支付完成时间
     */
    private LocalDateTime paidAt;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
