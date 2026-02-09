-- ========================================
-- 用户服务数据库表
-- Database: pfcbuy_user
-- ========================================

USE pfcbuy_user;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码（加密）',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别(0:未知,1:男,2:女)',
    birthday DATE COMMENT '生日',
    country VARCHAR(50) COMMENT '国家',
    language VARCHAR(20) DEFAULT 'zh-CN' COMMENT '语言偏好',
    currency VARCHAR(10) DEFAULT 'CNY' COMMENT '货币偏好',
    member_level VARCHAR(20) DEFAULT 'NORMAL' COMMENT '会员等级',
    balance DECIMAL(15,2) DEFAULT 0.00 COMMENT '账户余额',
    points INT DEFAULT 0 COMMENT '积分',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/LOCKED)',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    register_ip VARCHAR(50) COMMENT '注册IP',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户地址表
CREATE TABLE IF NOT EXISTS t_user_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    contact_name VARCHAR(100) NOT NULL COMMENT '收件人姓名',
    contact_phone VARCHAR(20) NOT NULL COMMENT '收件人电话',
    country VARCHAR(50) NOT NULL COMMENT '国家',
    province VARCHAR(50) COMMENT '省/州',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区/县',
    address VARCHAR(500) NOT NULL COMMENT '详细地址',
    zip_code VARCHAR(20) COMMENT '邮编',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认地址(0:否,1:是)',
    address_type VARCHAR(20) DEFAULT 'RECEIVING' COMMENT '地址类型(RECEIVING:收货,RETURN:退货)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_user_id (user_id),
    INDEX idx_is_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户地址表';

-- 用户登录日志表
CREATE TABLE IF NOT EXISTS t_user_login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    login_type VARCHAR(20) NOT NULL COMMENT '登录方式(PASSWORD/SMS/EMAIL/SOCIAL)',
    login_ip VARCHAR(50) COMMENT '登录IP',
    login_location VARCHAR(200) COMMENT '登录地点',
    user_agent VARCHAR(500) COMMENT '用户代理',
    device_type VARCHAR(50) COMMENT '设备类型',
    login_status VARCHAR(20) NOT NULL COMMENT '登录状态(SUCCESS/FAIL)',
    fail_reason VARCHAR(200) COMMENT '失败原因',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_login_status (login_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录日志表';
