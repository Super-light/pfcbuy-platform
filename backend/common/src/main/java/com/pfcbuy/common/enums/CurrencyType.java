package com.pfcbuy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货币类型枚举
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Getter
@AllArgsConstructor
public enum CurrencyType {
    CNY("CNY", "人民币", "¥"),
    USD("USD", "美元", "$"),
    EUR("EUR", "欧元", "€"),
    GBP("GBP", "英镑", "£"),
    JPY("JPY", "日元", "¥"),
    HKD("HKD", "港币", "HK$"),
    KRW("KRW", "韩元", "₩"),
    AUD("AUD", "澳元", "A$"),
    CAD("CAD", "加元", "C$");

    private final String code;
    private final String description;
    private final String symbol;

    /**
     * 根据code获取枚举
     */
    public static CurrencyType getByCode(String code) {
        for (CurrencyType currency : values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("未知的货币类型: " + code);
    }

    /**
     * 是否支持该货币
     */
    public static boolean isSupported(String code) {
        try {
            getByCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
