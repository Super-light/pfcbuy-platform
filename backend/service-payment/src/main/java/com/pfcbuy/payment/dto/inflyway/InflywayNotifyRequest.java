package com.pfcbuy.payment.dto.inflyway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 飞来汇支付回调通知
 *
 * 支付状态：
 * - 00: 待支付
 * - 01: 已支付
 * - 02: 部分退款
 * - 03: 全部退款
 * - 04: 支付单已失效
 * - 05: 支付失败
 * - 06: 支付处理中
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
public class InflywayNotifyRequest {

    /**
     * 商户ID
     */
    @JsonProperty("merchant_id")
    private String merchantId;

    /**
     * 订单号
     */
    @JsonProperty("order_no")
    private String orderNo;

    /**
     * 支付单ID
     */
    @JsonProperty("payment_order_id")
    private String paymentOrderId;

    /**
     * 业务系统流水号
     */
    @JsonProperty("invoice_id")
    private String invoiceId;

    /**
     * 货币类型
     */
    @JsonProperty("currency")
    private String currency;

    /**
     * 支付金额
     */
    @JsonProperty("amount")
    private String amount;

    /**
     * 支付状态
     * 00-待支付, 01-已支付, 02-部分退款, 03-全部退款,
     * 04-支付单已失效, 05-支付失败, 06-支付处理中
     */
    @JsonProperty("status")
    private String status;

    /**
     * 失败原因
     */
    @JsonProperty("failure_reason")
    private String failureReason;

    /**
     * 支付方式ID（如P00001 visa&mastercard）
     */
    @JsonProperty("pay_method_id")
    private String payMethodId;

    /**
     * 签名
     * 签名字段: amount + currency + invoice_id + merchant_id + payment_order_id + status
     */
    @JsonProperty("hash")
    private String hash;

    /**
     * 判断是否支付成功
     */
    public boolean isPaid() {
        return "01".equals(status);
    }

    /**
     * 判断是否支付失败
     */
    public boolean isFailed() {
        return "05".equals(status);
    }

    /**
     * 判断是否处理中
     */
    public boolean isProcessing() {
        return "00".equals(status) || "06".equals(status);
    }
}
