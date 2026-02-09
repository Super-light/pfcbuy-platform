package com.pfcbuy.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 飞来汇支付配置
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "inflyway")
public class InflywayConfig {

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * API Key
     */
    private String apiKey;

    /**
     * API Secret
     */
    private String apiSecret;

    /**
     * API 基础URL
     */
    private String apiBaseUrl = "https://api.inflyway.com";

    /**
     * 异步通知URL
     */
    private String notifyUrl;

    /**
     * 同步返回URL
     */
    private String returnUrl;

    /**
     * 支持的货币列表
     */
    private String supportedCurrencies = "USD,EUR,GBP,HKD,CNY";

    /**
     * 签名方式（MD5/RSA）
     */
    private String signType = "MD5";

    /**
     * 超时时间（分钟）
     */
    private Integer timeout = 30;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 获取支持的货币列表
     */
    public List<String> getSupportedCurrenciesList() {
        return Arrays.asList(supportedCurrencies.split(","));
    }
}
