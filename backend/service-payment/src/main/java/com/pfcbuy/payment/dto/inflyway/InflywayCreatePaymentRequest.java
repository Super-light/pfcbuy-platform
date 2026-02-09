package com.pfcbuy.payment.dto.inflyway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 飞来汇创建支付请求
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InflywayCreatePaymentRequest {

    /**
     * 商户ID（必传）
     */
    @JsonProperty("merchant_id")
    private String merchantId;

    /**
     * 业务系统流水号（必传，唯一，长度不超过50）
     */
    @JsonProperty("invoice_id")
    private String invoiceId;

    /**
     * 支付金额（必传）
     */
    @JsonProperty("amount")
    private String amount;

    /**
     * 货币类型（必传，如USD、CNY等）
     */
    @JsonProperty("currency")
    private String currency;

    /**
     * 异步通知地址（必传）
     */
    @JsonProperty("notify_url")
    private String notifyUrl;

    /**
     * 同步返回地址（必传）
     */
    @JsonProperty("return_url")
    private String returnUrl;

    /**
     * 签名（必传）
     * 签名字段：amount + currency + invoice_id + merchant_id
     */
    @JsonProperty("hash")
    private String hash;

    /**
     * 商品描述（可选）
     */
    @JsonProperty("description")
    private String description;

    /**
     * 客户邮箱（可选）
     */
    @JsonProperty("customer_email")
    private String customerEmail;

    /**
     * 客户电话（可选）
     */
    @JsonProperty("customer_phone")
    private String customerPhone;

    /**
     * 账单地址（可选，JSON字符串）
     */
    @JsonProperty("billing_address")
    private String billingAddress;

    /**
     * 收货地址（可选，JSON字符串）
     */
    @JsonProperty("shipping_address")
    private String shippingAddress;

    /**
     * 贸易子类型（可选，默认为DIGI）
     * DIGI - 数字商品
     * PHYS - 实物商品
     */
    @JsonProperty("trade_sub_type")
    private String tradeSubType;

    /**
     * 商品列表（可选，JSON字符串）
     */
    @JsonProperty("product_list")
    private String productList;

    /**
     * 扩展参数（可选，JSON字符串）
     */
    @JsonProperty("extend_params")
    private String extendParams;
}
