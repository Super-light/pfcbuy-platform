package com.pfcbuy.gateway.filter;

import com.pfcbuy.common.utils.JwtUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * JWT认证过滤器
 *
 * @author PfcBuy Team
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthFilter extends AbstractGatewayFilterFactory<Object> {

    private List<String> skipUrls;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String path = request.getPath().toString();

            // 检查是否需要跳过认证
            if (shouldSkipAuth(path)) {
                return chain.filter(exchange);
            }

            // 获取Token
            String token = getToken(request);

            if (!StringUtils.hasText(token)) {
                log.warn("请求路径 {} 缺少认证Token", path);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // 验证Token
            try {
                // 只验证token是否有效和未过期，不验证用户名
                if (JwtUtil.isTokenExpired(token)) {
                    log.warn("Token已过期，路径: {}", path);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                // 从Token中提取用户ID，并添加到请求头
                Long userId = JwtUtil.getUserIdFromToken(token);
                if (userId == null) {
                    log.warn("无法从Token中提取用户ID，路径: {}", path);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Id", userId.toString())
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                log.error("Token验证异常: {}", e.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        };
    }

    /**
     * 检查是否应该跳过认证
     */
    private boolean shouldSkipAuth(String path) {
        if (skipUrls == null || skipUrls.isEmpty()) {
            return false;
        }

        return skipUrls.stream()
                .anyMatch(pattern -> matchPath(pattern, path));
    }

    /**
     * 路径匹配（支持通配符）
     */
    private boolean matchPath(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }
        return pattern.equals(path);
    }

    /**
     * 从请求中获取Token
     */
    private String getToken(ServerHttpRequest request) {
        // 优先从Header中获取
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 从Query参数中获取
        List<String> tokenList = request.getQueryParams().get("token");
        if (tokenList != null && !tokenList.isEmpty()) {
            return tokenList.get(0);
        }

        return null;
    }
}