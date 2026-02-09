package com.pfcbuy.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付实体类
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_payment")
public class Payment extends BaseEntity {

    /**
     * 支付ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 支付流水号（系统生成）
     */
    private String paymentNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付渠道（STRIPE/INFLYWAY/ALIPAY/WECHAT）
     */
    private String paymentChannel;

    /**
     * 第三方交易ID（通用字段，各渠道的订单号）
     */
    private String thirdPartyTransactionId;

    /**
     * Stripe PaymentIntent ID
     */
    private String stripePaymentIntentId;

    /**
     * Stripe Charge ID
     */
    private String stripeChargeId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 货币类型（USD/EUR/GBP等）
     */
    private String currency;

    /**
     * 支付状态（INIT/PROCESSING/SUCCEEDED/FAILED/CANCELED/REFUNDED）
     */
    private String status;

    /**
     * 支付方式（card/alipay/wechat等）
     */
    private String paymentMethod;

    /**
     * 支付方式详情（JSON格式）
     */
    private String paymentMethodDetails;

    /**
     * 客户端密钥（用于前端确认支付）
     */
    private String clientSecret;

    /**
     * 支付完成时间
     */
    private LocalDateTime paidAt;

    /**
     * 取消时间
     */
    private LocalDateTime canceledAt;

    /**
     * 退款时间
     */
    private LocalDateTime refundedAt;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 失败原因
     */
    private String failureReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 飞来汇相关字段
     */

    /**
     * 飞来汇商户号
     */
    private String inflywayMerchantNo;

    /**
     * 飞来汇订单号
     */
    private String inflywayOrderNo;

    /**
     * 飞来汇交易流水号
     */
    private String inflywayTradeNo;

    /**
     * 飞来汇支付链接
     */
    private String inflywayPayUrl;

    /**
     * 支付链接
     */
    private String paymentUrl;

    /**
     * 元数据（JSON格式，存储额外信息）
     */
    private String metadata;
}
