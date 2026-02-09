package com.pfcbuy.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品解析服务启动类
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@MapperScan("com.pfcbuy.product.mapper")
@ComponentScan(basePackages = {"com.pfcbuy.product", "com.pfcbuy.common"})
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
