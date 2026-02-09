package com.pfcbuy.common.constants;

/**
 * 通用常量
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public class CommonConstants {

    /**
     * 成功标识
     */
    public static final int SUCCESS = 200;

    /**
     * 失败标识
     */
    public static final int FAIL = 500;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 禁止访问
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 参数错误
     */
    public static final int BAD_REQUEST = 400;

    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 用户ID请求头
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 是否删除标识
     */
    public static final int DELETED = 1;
    public static final int NOT_DELETED = 0;

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;
}
