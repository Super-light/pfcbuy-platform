package com.pfcbuy.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 支付服务启动类
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@MapperScan("com.pfcbuy.payment.mapper")
@EnableFeignClients(basePackages = "com.pfcbuy.payment.feign")
@ComponentScan(basePackages = {"com.pfcbuy.payment", "com.pfcbuy.common"})
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
