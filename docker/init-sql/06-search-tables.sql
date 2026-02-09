-- ========================================
-- 搜索服务数据库表
-- Database: pfcbuy_search
-- ========================================

USE pfcbuy_search;

-- 搜索历史表
CREATE TABLE IF NOT EXISTS t_search_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    keyword VARCHAR(500) NOT NULL COMMENT '搜索关键词',
    search_type VARCHAR(50) NOT NULL DEFAULT 'KEYWORD' COMMENT '搜索类型(KEYWORD/LINK/IMAGE/TAO_COMMAND)',
    result_count INT DEFAULT 0 COMMENT '搜索结果数量',
    user_ip VARCHAR(50) COMMENT '用户IP',
    user_agent VARCHAR(500) COMMENT '用户UA',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_user_id (user_id),
    INDEX idx_keyword (keyword(100)),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='搜索历史表';

-- 热门关键词表
CREATE TABLE IF NOT EXISTS t_hot_keyword (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    keyword VARCHAR(200) NOT NULL UNIQUE COMMENT '关键词',
    search_count BIGINT NOT NULL DEFAULT 0 COMMENT '搜索次数',
    weight INT NOT NULL DEFAULT 0 COMMENT '排序权重',
    visible TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示(0:否,1:是)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_search_count (search_count),
    INDEX idx_weight (weight),
    INDEX idx_visible (visible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热门关键词表';

-- 用户浏览记录表
CREATE TABLE IF NOT EXISTS t_browse_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_snapshot_id BIGINT NOT NULL COMMENT '商品快照ID',
    platform VARCHAR(50) NOT NULL COMMENT '平台类型',
    platform_product_id VARCHAR(200) NOT NULL COMMENT '平台商品ID',
    product_title VARCHAR(500) COMMENT '商品标题',
    product_image VARCHAR(500) COMMENT '商品图片',
    browse_duration INT DEFAULT 0 COMMENT '浏览时长（秒）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_user_id (user_id),
    INDEX idx_product_snapshot_id (product_snapshot_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户浏览记录表';

-- 用户收藏表
CREATE TABLE IF NOT EXISTS t_user_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_snapshot_id BIGINT NOT NULL COMMENT '商品快照ID',
    platform VARCHAR(50) NOT NULL COMMENT '平台类型',
    platform_product_id VARCHAR(200) NOT NULL COMMENT '平台商品ID',
    product_title VARCHAR(500) COMMENT '商品标题',
    product_image VARCHAR(500) COMMENT '商品图片',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    UNIQUE KEY uk_user_product (user_id, product_snapshot_id),
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';
