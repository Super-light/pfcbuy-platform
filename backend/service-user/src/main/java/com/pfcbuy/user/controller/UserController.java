package com.pfcbuy.user.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.user.entity.User;
import com.pfcbuy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Long userId) {
        log.info("获取用户信息: userId={}", userId);
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username/{username}")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email/{email}")
    public Result<Boolean> checkEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }
}
