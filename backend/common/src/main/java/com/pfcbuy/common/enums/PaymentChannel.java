package com.pfcbuy.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支付渠道枚举
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Getter
@RequiredArgsConstructor
public enum PaymentChannel {

    /**
     * Stripe 支付
     */
    STRIPE("STRIPE", "Stripe"),

    /**
     * 飞来汇支付
     */
    INFLYWAY("INFLYWAY", "飞来汇"),

    /**
     * 支付宝
     */
    ALIPAY("ALIPAY", "支付宝"),

    /**
     * 微信支付
     */
    WECHAT("WECHAT", "微信支付");

    private final String code;
    private final String description;

    /**
     * 根据 code 获取枚举
     */
    public static PaymentChannel fromCode(String code) {
        for (PaymentChannel channel : values()) {
            if (channel.getCode().equals(code)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("未知的支付渠道: " + code);
    }

    /**
     * 是否为第三方支付
     */
    public boolean isThirdParty() {
        return this != STRIPE;
    }
}
