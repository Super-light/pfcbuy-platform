package com.pfcbuy.logistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 物流服务启动类
 *
 * @author PfcBuy Team
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@MapperScan("com.pfcbuy.logistics.mapper")
@EnableFeignClients(basePackages = "com.pfcbuy.logistics.feign")
@ComponentScan(basePackages = {"com.pfcbuy.logistics", "com.pfcbuy.common"})
public class LogisticsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsServiceApplication.class, args);
    }
}
