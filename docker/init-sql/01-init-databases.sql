-- ========================================
-- PfcBuy Platform - 数据库初始化脚本
-- 创建所有微服务数据库
-- Author: PfcBuy Team
-- Date: 2024-02-05
-- ========================================

-- 用户服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_user 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '用户服务数据库';

-- 商品服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_product 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '商品服务数据库';

-- 订单服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_order 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '订单服务数据库';

-- 支付服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_payment 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '支付服务数据库';

-- 搜索服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_search 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '搜索服务数据库';

-- 仓储服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_warehouse 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '仓储服务数据库';

-- 物流服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_logistics 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci
    COMMENT '物流服务数据库';

-- Gateway服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_gateway
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci
    COMMENT 'API网关服务数据库';
