package com.pfcbuy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电商平台类型枚举
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Getter
@AllArgsConstructor
public enum PlatformType {
    TAOBAO("TAOBAO", "淘宝", "https://item.taobao.com"),
    TMALL("TMALL", "天猫", "https://detail.tmall.com"),
    JD("JD", "京东", "https://item.jd.com"),
    PINDUODUO("PINDUODUO", "拼多多", "https://mobile.yangkeduo.com"),
    WEIDIAN("WEIDIAN", "微店", "https://weidian.com");

    private final String code;
    private final String description;
    private final String urlPattern;

    /**
     * 根据URL判断平台类型
     */
    public static PlatformType fromUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL不能为空");
        }
        
        for (PlatformType platform : values()) {
            if (url.contains(platform.getUrlPattern())) {
                return platform;
            }
        }
        
        throw new IllegalArgumentException("不支持的平台URL: " + url);
    }

    /**
     * 根据code获取枚举
     */
    public static PlatformType getByCode(String code) {
        for (PlatformType platform : values()) {
            if (platform.getCode().equalsIgnoreCase(code)) {
                return platform;
            }
        }
        throw new IllegalArgumentException("未知的平台类型: " + code);
    }
}
