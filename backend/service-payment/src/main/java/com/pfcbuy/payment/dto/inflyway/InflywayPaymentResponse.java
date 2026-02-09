package com.pfcbuy.payment.dto.inflyway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 飞来汇支付响应
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
public class InflywayPaymentResponse {

    /**
     * 响应码（2000表示成功）
     */
    @JsonProperty("code")
    private String code;

    /**
     * 响应消息
     */
    @JsonProperty("message")
    private String message;

    /**
     * 是否成功
     */
    @JsonProperty("success")
    private Boolean success;

    /**
     * 是否处理中
     */
    @JsonProperty("processing")
    private Boolean processing;

    /**
     * 是否失败
     */
    @JsonProperty("failed")
    private Boolean failed;

    /**
     * 请求编号
     */
    @JsonProperty("reqNo")
    private String reqNo;

    /**
     * 响应数据
     */
    @JsonProperty("data")
    private PaymentData data;

    @Data
    public static class PaymentData {
        /**
         * 支付单ID
         */
        @JsonProperty("payment_order_id")
        private String paymentOrderId;

        /**
         * 支付链接（用于跳转支付页面）
         */
        @JsonProperty("payment_url")
        private String paymentUrl;

        /**
         * 二维码链接（如果需要的话）
         */
        @JsonProperty("qr_code_url")
        private String qrCodeUrl;

        /**
         * 交易流水号
         */
        @JsonProperty("trade_no")
        private String tradeNo;

        /**
         * 支付时间
         */
        @JsonProperty("pay_time")
        private String payTime;

        /**
         * 支付状态
         */
        @JsonProperty("status")
        private String status;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(success) && "2000".equals(code);
    }

    /**
     * 判断是否已支付
     */
    public boolean isPaid() {
        return data != null && "PAID".equals(data.getStatus());
    }
}