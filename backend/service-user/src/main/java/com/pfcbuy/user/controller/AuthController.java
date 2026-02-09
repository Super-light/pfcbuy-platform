package com.pfcbuy.user.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.user.dto.LoginRequest;
import com.pfcbuy.user.dto.LoginResponse;
import com.pfcbuy.user.dto.RegisterRequest;
import com.pfcbuy.user.entity.User;
import com.pfcbuy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        log.info("注册请求: username={}", request.getUsername());
        User user = userService.register(request);
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("登录请求: username={}", request.getUsername());
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }
    
    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        log.info("刷新Token请求");
        
        // 提取Token（去掉 "Bearer " 前缀）
        String refreshToken = authHeader.replace("Bearer ", "");
        LoginResponse response = userService.refreshToken(refreshToken);
        return Result.success(response);
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("User Service is running");
    }
}
