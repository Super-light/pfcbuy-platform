package com.pfcbuy.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 搜索服务启动类
 *
 * @author PfcBuy Team
 * @date 2024-02-04
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@MapperScan("com.pfcbuy.search.mapper")
@EnableFeignClients(basePackages = "com.pfcbuy.search.feign")
@ComponentScan(basePackages = {"com.pfcbuy.search", "com.pfcbuy.common"})
public class SearchServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }
}
