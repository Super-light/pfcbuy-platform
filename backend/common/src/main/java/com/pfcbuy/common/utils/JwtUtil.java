package com.pfcbuy.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Slf4j
public class JwtUtil {

    // 默认密钥（生产环境请使用配置文件）
    private static final String DEFAULT_SECRET_KEY = "pfcbuy-platform-secret-key-2024-very-long-secret-key-for-jwt-token";
    
    // Token有效期（24小时）
    private static final long DEFAULT_EXPIRATION_TIME = 86400000L;

    /**
     * 生成密钥
     */
    private static Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成Token（使用默认配置）
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT token
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, DEFAULT_SECRET_KEY, DEFAULT_EXPIRATION_TIME);
    }

    /**
     * 生成Token（带自定义密钥和过期时间）
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param secretKey 密钥
     * @param expiration 过期时间（毫秒）
     * @return JWT token
     */
    public static String generateToken(Long userId, String username, String secretKey, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, username, secretKey, expiration);
    }

    /**
     * 生成Token（带额外声明，使用默认配置）
     *
     * @param claims 声明
     * @param subject 主题
     * @return JWT token
     */
    public static String createToken(Map<String, Object> claims, String subject) {
        return createToken(claims, subject, DEFAULT_SECRET_KEY, DEFAULT_EXPIRATION_TIME);
    }

    /**
     * 生成Token（带额外声明，自定义配置）
     *
     * @param claims 声明
     * @param subject 主题
     * @param secretKey 密钥
     * @param expiration 过期时间（毫秒）
     * @return JWT token
     */
    public static String createToken(Map<String, Object> claims, String subject, String secretKey, Long expiration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从Token中获取Claims（使用默认密钥）
     *
     * @param token JWT token
     * @return Claims
     */
    public static Claims getClaimsFromToken(String token) {
        return getClaimsFromToken(token, DEFAULT_SECRET_KEY);
    }

    /**
     * 从Token中获取Claims（使用自定义密钥）
     *
     * @param token JWT token
     * @param secretKey 密钥
     * @return Claims
     */
    public static Claims getClaimsFromToken(String token, String secretKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取用户ID（使用默认密钥）
     *
     * @param token JWT token
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        return getUserIdFromToken(token, DEFAULT_SECRET_KEY);
    }

    /**
     * 从Token中获取用户ID（使用自定义密钥）
     *
     * @param token JWT token
     * @param secretKey 密钥
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token, String secretKey) {
        Claims claims = getClaimsFromToken(token, secretKey);
        if (claims != null) {
            Object userId = claims.get("userId");
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            } else if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * 从Token中获取用户名（使用默认密钥）
     *
     * @param token JWT token
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        return getUsernameFromToken(token, DEFAULT_SECRET_KEY);
    }

    /**
     * 从Token中获取用户名（使用自定义密钥）
     *
     * @param token JWT token
     * @param secretKey 密钥
     * @return 用户名
     */
    public static String getUsernameFromToken(String token, String secretKey) {
        Claims claims = getClaimsFromToken(token, secretKey);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 验证Token是否过期（使用默认密钥）
     *
     * @param token JWT token
     * @return true-已过期 false-未过期
     */
    public static boolean isTokenExpired(String token) {
        return isTokenExpired(token, DEFAULT_SECRET_KEY);
    }

    /**
     * 验证Token是否过期（使用自定义密钥）
     *
     * @param token JWT token
     * @param secretKey 密钥
     * @return true-已过期 false-未过期
     */
    public static boolean isTokenExpired(String token, String secretKey) {
        try {
            Claims claims = getClaimsFromToken(token, secretKey);
            if (claims == null) {
                return true;
            }
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证Token是否有效（使用默认密钥）
     *
     * @param token JWT token
     * @param username 用户名
     * @return true-有效 false-无效
     */
    public static boolean validateToken(String token, String username) {
        return validateToken(token, username, DEFAULT_SECRET_KEY);
    }

    /**
     * 验证Token是否有效（使用自定义密钥）
     *
     * @param token JWT token
     * @param username 用户名
     * @param secretKey 密钥
     * @return true-有效 false-无效
     */
    public static boolean validateToken(String token, String username, String secretKey) {
        try {
            String tokenUsername = getUsernameFromToken(token, secretKey);
            return username.equals(tokenUsername) && !isTokenExpired(token, secretKey);
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage());
            return false;
        }
    }
}