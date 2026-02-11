package com.pfcbuy.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;

/**
 * API网关启动类
 *
 * @author PfcBuy Team
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@ComponentScan(basePackages = {"com.pfcbuy.gateway", "com.pfcbuy.common"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.REACTIVE);
        app.run(args);
    }
}
