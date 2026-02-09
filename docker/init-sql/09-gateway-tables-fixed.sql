-- ========================================
-- API网关服务数据库表
-- Database: pfcbuy_gateway
-- ========================================

USE pfcbuy_gateway;

-- API访问日志表
CREATE TABLE IF NOT EXISTS t_api_access_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    trace_id VARCHAR(64) NOT NULL COMMENT '链路追踪ID',
    user_id BIGINT COMMENT '用户ID',
    user_ip VARCHAR(50) COMMENT '用户IP',
    request_method VARCHAR(10) NOT NULL COMMENT '请求方法(GET/POST/PUT/DELETE)',
    request_uri VARCHAR(500) NOT NULL COMMENT '请求URI',
    request_params TEXT COMMENT '请求参数',
    request_body TEXT COMMENT '请求体（限制大小）',
    request_headers TEXT COMMENT '请求头（JSON）',
    response_status INT NOT NULL COMMENT 'HTTP状态码',
    response_body TEXT COMMENT '响应体（限制大小）',
    response_time INT NOT NULL COMMENT '响应时间（毫秒）',
    error_message TEXT COMMENT '错误信息',
    user_agent VARCHAR(500) COMMENT '用户代理',
    service_name VARCHAR(100) COMMENT '目标服务名',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_trace_id (trace_id),
    INDEX idx_user_id (user_id),
    INDEX idx_user_ip (user_ip),
    INDEX idx_request_uri (request_uri(100)),
    INDEX idx_response_status (response_status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API访问日志表';

-- 限流配置表
CREATE TABLE IF NOT EXISTS t_rate_limit_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    config_key VARCHAR(200) NOT NULL UNIQUE COMMENT '配置键（IP/USER/API）',
    config_type VARCHAR(50) NOT NULL COMMENT '限流类型(IP/USER/API/GLOBAL)',
    limit_count INT NOT NULL DEFAULT 100 COMMENT '限流次数',
    limit_period INT NOT NULL DEFAULT 60 COMMENT '限流周期（秒）',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用(0:否,1:是)',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_config_key (config_key),
    INDEX idx_config_type (config_type),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='限流配置表';

-- IP黑名单表
CREATE TABLE IF NOT EXISTS t_ip_blacklist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    ip_address VARCHAR(50) NOT NULL UNIQUE COMMENT 'IP地址',
    block_reason VARCHAR(500) COMMENT '封禁原因',
    block_type VARCHAR(50) NOT NULL DEFAULT 'PERMANENT' COMMENT '封禁类型(PERMANENT/TEMPORARY)',
    block_until DATETIME COMMENT '封禁截止时间（临时封禁）',
    block_by VARCHAR(100) COMMENT '封禁操作人',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用(0:否,1:是)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_ip_address (ip_address),
    INDEX idx_enabled (enabled),
    INDEX idx_block_until (block_until)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IP黑名单表';

-- IP白名单表
CREATE TABLE IF NOT EXISTS t_ip_whitelist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    ip_address VARCHAR(50) NOT NULL UNIQUE COMMENT 'IP地址',
    white_reason VARCHAR(500) COMMENT '白名单原因',
    added_by VARCHAR(100) COMMENT '添加人',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用(0:否,1:是)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_ip_address (ip_address),
    INDEX idx_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IP白名单表';

-- Token黑名单表（用于JWT注销）
CREATE TABLE IF NOT EXISTS t_token_blacklist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    token_jti VARCHAR(100) NOT NULL UNIQUE COMMENT 'JWT的JTI（唯一标识）',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    token_type VARCHAR(50) NOT NULL DEFAULT 'ACCESS' COMMENT 'Token类型(ACCESS/REFRESH)',
    logout_reason VARCHAR(200) COMMENT '注销原因(USER_LOGOUT/ADMIN_FORCE/SECURITY)',
    expired_time DATETIME NOT NULL COMMENT 'Token过期时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_token_jti (token_jti),
    INDEX idx_user_id (user_id),
    INDEX idx_expired_time (expired_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Token黑名单表';

-- API路由配置表（动态路由）
CREATE TABLE IF NOT EXISTS t_api_route_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_id VARCHAR(100) NOT NULL UNIQUE COMMENT '路由ID',
    route_name VARCHAR(200) NOT NULL COMMENT '路由名称',
    route_uri VARCHAR(500) NOT NULL COMMENT '目标URI',
    predicates TEXT NOT NULL COMMENT '断言配置（JSON）',
    filters TEXT COMMENT '过滤器配置（JSON）',
    order_num INT DEFAULT 0 COMMENT '路由顺序',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用(0:否,1:是)',
    metadata TEXT COMMENT '元数据（JSON）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_route_id (route_id),
    INDEX idx_enabled (enabled),
    INDEX idx_order_num (order_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API路由配置表';

-- API限流记录表（用于统计）
CREATE TABLE IF NOT EXISTS t_rate_limit_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    limit_key VARCHAR(200) NOT NULL COMMENT '限流键',
    limit_type VARCHAR(50) NOT NULL COMMENT '限流类型',
    request_count INT NOT NULL DEFAULT 1 COMMENT '请求次数',
    blocked_count INT NOT NULL DEFAULT 0 COMMENT '被拦截次数',
    record_time DATETIME NOT NULL COMMENT '记录时间（精确到分钟）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_limit_record (limit_key, record_time),
    INDEX idx_limit_key (limit_key),
    INDEX idx_record_time (record_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API限流记录表';

-- 系统监控指标表
CREATE TABLE IF NOT EXISTS t_system_metrics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    metric_type VARCHAR(50) NOT NULL COMMENT '指标类型(QPS/RT/ERROR_RATE)',
    metric_value DECIMAL(15,2) NOT NULL COMMENT '指标值',
    service_name VARCHAR(100) COMMENT '服务名称',
    record_time DATETIME NOT NULL COMMENT '记录时间（精确到分钟）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_metrics (metric_type, service_name, record_time),
    INDEX idx_metric_type (metric_type),
    INDEX idx_service_name (service_name),
    INDEX idx_record_time (record_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统监控指标表';

-- 初始化限流配置数据
INSERT INTO t_rate_limit_config (config_key, config_type, limit_count, limit_period, enabled, remark) VALUES
('global', 'GLOBAL', 10000, 60, 1, '全局限流：每分钟10000次'),
('default_api', 'API', 1000, 60, 1, '默认API限流：每分钟1000次'),
('default_user', 'USER', 100, 60, 1, '默认用户限流：每分钟100次'),
('default_ip', 'IP', 200, 60, 1, '默认IP限流：每分钟200次');
