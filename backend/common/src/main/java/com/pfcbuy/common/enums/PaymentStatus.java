package com.pfcbuy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    UNPAID("UNPAID", "待支付"),
    INIT("INIT", "初始化"),
    PROCESSING("PROCESSING", "处理中"),
    PAID("PAID", "已支付"),
    FAILED("FAILED", "支付失败"),
    REFUNDED("REFUNDED", "已退款");

    private final String code;
    private final String description;

    /**
     * 根据码获取描述
     */
    public static String getDescByCode(String code) {
        for (PaymentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDescription();
            }
        }
        return "未知状态";
    }

    /**
     * 根据码获取枚举
     */
    public static PaymentStatus getByCode(String code) {
        for (PaymentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的支付状态: " + code);
    }

    /**
     * 是否为成功状态
     */
    public boolean isSuccess() {
        return this == PAID || this == REFUNDED;
    }
}
