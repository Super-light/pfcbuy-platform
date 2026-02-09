package com.pfcbuy.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
public class LoginRequest {
    
    /**
     * 用户名或邮箱
     */
    @NotBlank(message = "用户名/邮箱不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 记住我（7天有效）
     */
    private Boolean rememberMe = false;
}
