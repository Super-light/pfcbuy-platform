package com.pfcbuy.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务启动类
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@MapperScan("com.pfcbuy.order.mapper")
@EnableFeignClients(basePackages = "com.pfcbuy.order.feign")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
