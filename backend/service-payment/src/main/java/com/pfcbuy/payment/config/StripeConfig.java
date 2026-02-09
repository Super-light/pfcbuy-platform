package com.pfcbuy.payment.config;

import com.stripe.Stripe;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Stripe 配置
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {

    /**
     * Stripe API Key
     */
    private String apiKey;

    /**
     * Webhook Secret
     */
    private String webhookSecret;

    /**
     * 支持的货币列表
     */
    private String supportedCurrencies;

    /**
     * 支付成功回调URL
     */
    private String successUrl;

    /**
     * 支付取消回调URL
     */
    private String cancelUrl;

    /**
     * 初始化 Stripe API Key
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = this.apiKey;
    }

    /**
     * 获取支持的货币列表
     */
    public List<String> getSupportedCurrenciesList() {
        return Arrays.asList(supportedCurrencies.split(","));
    }
}
