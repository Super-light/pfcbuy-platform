package com.pfcbuy.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 字符串工具类
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public class StringUtil {

    /**
     * 生成UUID（去掉横杠）
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成订单号
     * 格式：前缀 + 时间戳 + 4位随机数
     */
    public static String generateOrderNo(String prefix) {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 9000) + 1000;
        return prefix + timestamp + random;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 判断字符串是否为空或空白
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 判断字符串是否不为空或空白
     */
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 脱敏手机号
     * 138****8888
     */
    public static String desensitizePhone(String phone) {
        if (isEmpty(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 脱敏邮箱
     * abc***@gmail.com
     */
    public static String desensitizeEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 3) {
            return username.charAt(0) + "***@" + parts[1];
        }
        return username.substring(0, 3) + "***@" + parts[1];
    }

    /**
     * 截取字符串
     */
    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }
}
