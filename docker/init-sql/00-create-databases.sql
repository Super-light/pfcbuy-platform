-- 创建用户服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建商品服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_product DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建订单服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建支付服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_payment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建搜索服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_search DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建仓库服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_warehouse DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建物流服务数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_logistics DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建网关数据库
CREATE DATABASE IF NOT EXISTS pfcbuy_gateway DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
