package com.pfcbuy.payment.client;

import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.common.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 飞来汇HTTP客户端
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InflywayHttpClient {

    private final RestTemplate restTemplate;

    /**
     * POST 请求（带认证头）
     *
     * @param url           请求URL
     * @param requestBody   请求体
     * @param merchantId    商户ID
     * @param secretKey     商户密钥
     * @param userAgent     User-Agent
     * @param responseClass 响应类型
     * @return 响应对象
     */
    public <T> T postWithAuth(String url, Object requestBody, String merchantId,
                              String secretKey, String userAgent, Class<T> responseClass) {
        try {
            log.info("飞来汇API请求: URL={}, Body={}", url, JsonUtil.toJson(requestBody));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 添加Basic Auth
            String credentials = merchantId + ":" + secretKey;
            String encodedCredentials = java.util.Base64.getEncoder()
                    .encodeToString(credentials.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedCredentials);

            // 添加user-agent
            if (userAgent != null && !userAgent.isEmpty()) {
                headers.set("user-agent", userAgent);
            }

            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    responseClass
            );

            log.info("飞来汇API响应: Status={}, Body={}",
                    response.getStatusCode(), JsonUtil.toJson(response.getBody()));

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new BusinessException("飞来汇API调用失败: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("飞来汇API调用异常: {}", e.getMessage(), e);
            throw new BusinessException("飞来汇API调用异常: " + e.getMessage());
        }
    }

    /**
     * GET 请求
     *
     * @param url           请求URL
     * @param params        请求参数
     * @param responseClass 响应类型
     * @return 响应对象
     */
    public <T> T get(String url, Map<String, Object> params, Class<T> responseClass) {
        try {
            // 构建查询参数
            StringBuilder urlBuilder = new StringBuilder(url);
            if (params != null && !params.isEmpty()) {
                urlBuilder.append("?");
                params.forEach((key, value) -> urlBuilder.append(key).append("=").append(value).append("&"));
                // 移除最后一个 &
                urlBuilder.setLength(urlBuilder.length() - 1);
            }

            String fullUrl = urlBuilder.toString();
            log.info("飞来汇API GET请求: URL={}", fullUrl);

            ResponseEntity<T> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.GET,
                    null,
                    responseClass
            );

            log.info("飞来汇API响应: Status={}, Body={}", response.getStatusCode(), JsonUtil.toJson(response.getBody()));

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new BusinessException("飞来汇API调用失败: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("飞来汇API调用异常: {}", e.getMessage(), e);
            throw new BusinessException("飞来汇API调用异常: " + e.getMessage());
        }
    }
}
