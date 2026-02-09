-- ========================================
-- 商品服务数据库表
-- Database: pfcbuy_product
-- ========================================

USE pfcbuy_product;

-- 商品快照表
CREATE TABLE IF NOT EXISTS t_product_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    platform VARCHAR(50) NOT NULL COMMENT '平台类型(TAOBAO/TMALL/JD/1688)',
    platform_product_id VARCHAR(200) NOT NULL COMMENT '平台商品ID',
    platform_sku_id VARCHAR(200) COMMENT '平台SKU ID',
    title VARCHAR(500) NOT NULL COMMENT '商品标题',
    price DECIMAL(15,2) NOT NULL COMMENT '商品价格',
    original_price DECIMAL(15,2) COMMENT '原价',
    currency VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
    main_image VARCHAR(500) COMMENT '主图URL',
    images TEXT COMMENT '商品图片（JSON数组）',
    sku_attributes TEXT COMMENT 'SKU属性（JSON）',
    stock INT DEFAULT 0 COMMENT '库存数量',
    sales INT DEFAULT 0 COMMENT '销量',
    product_url TEXT COMMENT '商品链接',
    shop_name VARCHAR(200) COMMENT '店铺名称',
    shop_url VARCHAR(500) COMMENT '店铺链接',
    category VARCHAR(200) COMMENT '分类',
    brand VARCHAR(200) COMMENT '品牌',
    snapshot_time DATETIME NOT NULL COMMENT '快照时间',
    available TINYINT DEFAULT 1 COMMENT '是否有效(0:否,1:是)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    UNIQUE KEY uk_platform_product (platform, platform_product_id, platform_sku_id),
    INDEX idx_platform (platform),
    INDEX idx_snapshot_time (snapshot_time),
    INDEX idx_available (available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品快照表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS t_product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    name_en VARCHAR(100) COMMENT '英文名称',
    level INT DEFAULT 1 COMMENT '层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    icon VARCHAR(500) COMMENT '图标',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';
