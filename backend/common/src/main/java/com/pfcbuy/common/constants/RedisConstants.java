package com.pfcbuy.common.constants;

/**
 * Redis缓存Key常量
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public class RedisConstants {

    /**
     * 缓存过期时间
     */
    public static final long CACHE_EXPIRE_MINUTE = 60L;
    public static final long CACHE_EXPIRE_HOUR = 3600L;
    public static final long CACHE_EXPIRE_DAY = 86400L;

    /**
     * 商品快照缓存Key前缀
     */
    public static final String PRODUCT_SNAPSHOT_KEY = "product:snapshot:";

    /**
     * 商品快照缓存过期时间（1小时）
     */
    public static final long PRODUCT_SNAPSHOT_EXPIRE = CACHE_EXPIRE_HOUR;

    /**
     * 订单缓存Key前缀
     */
    public static final String ORDER_KEY = "order:";

    /**
     * 订单缓存过期时间（30分钟）
     */
    public static final long ORDER_EXPIRE = 30 * CACHE_EXPIRE_MINUTE;

    /**
     * 用户Token缓存Key前缀
     */
    public static final String USER_TOKEN_KEY = "user:token:";

    /**
     * Token缓存过期时间（24小时）
     */
    public static final long USER_TOKEN_EXPIRE = CACHE_EXPIRE_DAY;

    /**
     * 汇率缓存Key前缀
     */
    public static final String EXCHANGE_RATE_KEY = "exchange:rate:";

    /**
     * 汇率缓存过期时间（1小时）
     */
    public static final long EXCHANGE_RATE_EXPIRE = CACHE_EXPIRE_HOUR;

    /**
     * 分布式锁Key前缀
     */
    public static final String LOCK_KEY = "lock:";

    /**
     * 分布式锁默认过期时间（30秒）
     */
    public static final long LOCK_EXPIRE = 30L;
}
