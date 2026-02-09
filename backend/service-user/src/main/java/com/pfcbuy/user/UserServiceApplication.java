package com.pfcbuy.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@SpringBootApplication(scanBasePackages = "com.pfcbuy", exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
@MapperScan("com.pfcbuy.user.mapper")
@ComponentScan(basePackages = {"com.pfcbuy.user", "com.pfcbuy.common"})
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
