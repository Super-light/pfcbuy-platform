package com.pfcbuy.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 创建支付请求
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
public class CreatePaymentRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    @Positive(message = "支付金额必须大于0")
    private BigDecimal amount;

    /**
     * 货币类型（USD/EUR/GBP等）
     */
    @NotBlank(message = "货币类型不能为空")
    private String currency;

    /**
     * 支付渠道（STRIPE/INFLYWAY）
     */
    private String paymentChannel = "STRIPE"; // 默认Stripe

    /**
     * 支付方式（可选：card/alipay/wechat）
     */
    private String paymentMethod;

    /**
     * 用户IP地址
     */
    private String userIp;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 成功回调URL
     */
    private String successUrl;

    /**
     * 取消回调URL
     */
    private String cancelUrl;

    /**
     * 客户邮箱（可选）
     */
    private String customerEmail;

    /**
     * 额外元数据
     */
    private Map<String, String> metadata;
}
