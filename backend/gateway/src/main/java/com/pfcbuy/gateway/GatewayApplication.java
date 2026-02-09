package com.pfcbuy.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * API网关启动类
 *
 * @author PfcBuy Team
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@EnableDiscoveryClient
@MapperScan("com.pfcbuy.gateway.mapper")
@ComponentScan(basePackages = {"com.pfcbuy.gateway", "com.pfcbuy.common"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
