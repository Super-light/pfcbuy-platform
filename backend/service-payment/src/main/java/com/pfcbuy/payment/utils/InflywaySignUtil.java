package com.pfcbuy.payment.utils;

import com.pfcbuy.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 飞来汇签名工具类
 *
 * 签名规则：
 * 1. 将商户密钥放在第一位
 * 2. 按key升序排列参数
 * 3. 排除空值和hash字段
 * 4. 拼接成字符串: secret + key1 + value1 + key2 + value2...
 * 5. 使用SHA-256加密并转大写
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
public class InflywaySignUtil {

    /**
     * 生成飞来汇签名（SHA-256方式）
     *
     * 签名字段按文档说明：
     * - 创建支付单: amount + currency + invoice_id + merchant_id
     * - 支付结果: amount + currency + invoice_id + merchant_id + payment_order_id + status
     * - 退款: currency + invoice_id + merchant_id + refund_amount
     *
     * @param params    参数Map（已按顺序）
     * @param apiSecret API密钥
     * @return 签名字符串（大写）
     */
    public static String generateSign(Map<String, Object> params, String apiSecret) {
        try {
            // 1. 过滤空值和hash字段
            Map<String, Object> filteredParams = params.entrySet().stream()
                    .filter(entry -> entry.getValue() != null
                            && !"hash".equals(entry.getKey())
                            && !"".equals(String.valueOf(entry.getValue()).trim()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            // 2. 按key升序排列
            TreeMap<String, Object> sortedParams = new TreeMap<>(filteredParams);

            // 3. 拼接字符串：secret + key1 + value1 + key2 + value2
            StringBuilder signBuilder = new StringBuilder(apiSecret);
            for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
                signBuilder.append(entry.getKey()).append(entry.getValue());
            }

            String signStr = signBuilder.toString();
            log.debug("签名原始字符串: {}", signStr);

            // 4. SHA-256加密
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signStr.getBytes(StandardCharsets.UTF_8));

            // 5. 转为16进制大写字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String sign = hexString.toString().toUpperCase();
            log.debug("生成的签名: {}", sign);

        return sign;

        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256算法不可用", e);
            throw new RuntimeException("签名生成失败", e);
        }
    }

    /**
     * 创建支付单签名
     * 签名字段: amount + currency + invoice_id + merchant_id
     */
    public static String createPaymentSign(String amount, String currency, String invoiceId,
                                           String merchantId, String apiSecret) {
        Map<String, Object> params = new TreeMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("invoice_id", invoiceId);
        params.put("merchant_id", merchantId);

        return generateSign(params, apiSecret);
    }

    /**
     * 支付结果验签
     * 签名字段: amount + currency + invoice_id + merchant_id + payment_order_id + status
     */
    public static String paymentResultSign(String amount, String currency, String invoiceId,
                                           String merchantId, String paymentOrderId,
                                           String status, String apiSecret) {
        Map<String, Object> params = new TreeMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("invoice_id", invoiceId);
        params.put("merchant_id", merchantId);
        params.put("payment_order_id", paymentOrderId);
        params.put("status", status);

        return generateSign(params, apiSecret);
    }

    /**
     * 退款签名
     * 签名字段: currency + invoice_id + merchant_id + refund_amount
     */
    public static String refundSign(String currency, String invoiceId, String merchantId,
                                    String refundAmount, String apiSecret) {
        Map<String, Object> params = new TreeMap<>();
        params.put("currency", currency);
        params.put("invoice_id", invoiceId);
        params.put("merchant_id", merchantId);
        params.put("refund_amount", refundAmount);

        return generateSign(params, apiSecret);
    }

    /**
     * 验证签名
     *
     * @param params    参数Map
     * @param sign      待验证的签名
     * @param apiSecret API密钥
     * @return 是否验证通过
     */
    public static boolean verifySign(Map<String, Object> params, String sign, String apiSecret) {
        String calculatedSign = generateSign(params, apiSecret);
        boolean valid = calculatedSign.equalsIgnoreCase(sign);

        if (!valid) {
            log.warn("签名验证失败！计算签名: {}, 接收签名: {}", calculatedSign, sign);
        }
        
        return valid;
    }

    /**
     * 对象转Map（用于签名）
     *
     * @param obj 对象
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> objectToMap(Object obj) {
        String json = JsonUtil.toJson(obj);
        return JsonUtil.fromJson(json, Map.class);
    }

    /**
     * 生成Basic Auth头
     *
     * @param merchantId 商户ID
     * @param secretKey  商户密钥
     * @return Basic Auth字符串
     */
    public static String generateBasicAuth(String merchantId, String secretKey) {
        String credentials = merchantId + ":" + secretKey;
        String encoded = java.util.Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }
}
