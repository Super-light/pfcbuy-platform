package com.pfcbuy.user.service;

import com.pfcbuy.user.dto.LoginRequest;
import com.pfcbuy.user.dto.LoginResponse;
import com.pfcbuy.user.dto.RegisterRequest;
import com.pfcbuy.user.entity.User;

/**
 * 用户服务接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
public interface UserService {
    
    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册成功的用户信息
     */
    User register(RegisterRequest request);
    
    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应（包含Token）
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 刷新Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    LoginResponse refreshToken(String refreshToken);
    
    /**
     * 根据ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 根据用户名或邮箱获取用户
     *
     * @param usernameOrEmail 用户名或邮箱
     * @return 用户信息
     */
    User getUserByUsernameOrEmail(String usernameOrEmail);
    
    /**
     * 更新用户最后登录信息
     *
     * @param userId 用户ID
     * @param ip 登录IP
     */
    void updateLastLogin(Long userId, String ip);
    
    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return true-存在 false-不存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return true-存在 false-不存在
     */
    boolean existsByEmail(String email);
}
