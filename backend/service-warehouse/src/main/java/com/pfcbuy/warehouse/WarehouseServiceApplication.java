package com.pfcbuy.warehouse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 仓储服务启动类
 *
 * @author PfcBuy Team
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy")
@MapperScan("com.pfcbuy.warehouse.mapper")
public class WarehouseServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WarehouseServiceApplication.class, args);
    }
}