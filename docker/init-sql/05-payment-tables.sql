-- ========================================
-- 支付服务数据库表
-- Database: pfcbuy_payment
-- ========================================

USE pfcbuy_payment;

-- 支付记录表
CREATE TABLE IF NOT EXISTS t_payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    payment_no VARCHAR(64) NOT NULL UNIQUE COMMENT '支付单号',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    payment_channel VARCHAR(50) NOT NULL COMMENT '支付渠道(STRIPE/INFLYWAY)',
    payment_method VARCHAR(50) COMMENT '支付方式(card/alipay/wechat)',
    amount DECIMAL(15,2) NOT NULL COMMENT '支付金额',
    currency VARCHAR(10) NOT NULL DEFAULT 'USD' COMMENT '货币类型',
    exchange_rate DECIMAL(10,4) DEFAULT 1.0000 COMMENT '汇率',
    status VARCHAR(50) NOT NULL DEFAULT 'INIT' COMMENT '支付状态',
    third_party_payment_id VARCHAR(200) COMMENT '第三方支付ID',
    third_party_charge_id VARCHAR(200) COMMENT '第三方交易ID',
    client_secret VARCHAR(500) COMMENT '客户端密钥',
    payment_url TEXT COMMENT '支付链接',
    callback_url VARCHAR(500) COMMENT '回调URL',
    return_url VARCHAR(500) COMMENT '返回URL',
    user_ip VARCHAR(50) COMMENT '用户IP',
    paid_time DATETIME COMMENT '支付完成时间',
    expired_time DATETIME COMMENT '过期时间',
    notify_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '通知状态',
    notify_times INT DEFAULT 0 COMMENT '通知次数',
    last_notify_time DATETIME COMMENT '最后通知时间',
    remark VARCHAR(500) COMMENT '备注',
    metadata TEXT COMMENT '元数据（JSON）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_payment_no (payment_no),
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_payment_channel (payment_channel),
    INDEX idx_third_party_payment_id (third_party_payment_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- 退款记录表
CREATE TABLE IF NOT EXISTS t_refund (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    refund_no VARCHAR(64) NOT NULL UNIQUE COMMENT '退款单号',
    payment_no VARCHAR(64) NOT NULL COMMENT '支付单号',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    payment_channel VARCHAR(50) NOT NULL COMMENT '支付渠道',
    refund_amount DECIMAL(15,2) NOT NULL COMMENT '退款金额',
    currency VARCHAR(10) NOT NULL COMMENT '货币类型',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT '退款状态',
    third_party_refund_id VARCHAR(200) COMMENT '第三方退款ID',
    refund_reason VARCHAR(500) COMMENT '退款原因',
    refund_time DATETIME COMMENT '退款完成时间',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_refund_no (refund_no),
    INDEX idx_payment_no (payment_no),
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款记录表';

-- 用户余额表
CREATE TABLE IF NOT EXISTS t_user_balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '余额',
    frozen_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '冻结金额',
    total_recharge DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '累计充值',
    total_consume DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '累计消费',
    version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户余额表';

-- 余额变动记录表
CREATE TABLE IF NOT EXISTS t_balance_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    change_type VARCHAR(50) NOT NULL COMMENT '变动类型(RECHARGE/CONSUME/REFUND/WITHDRAW)',
    amount DECIMAL(15,2) NOT NULL COMMENT '变动金额',
    balance_before DECIMAL(15,2) NOT NULL COMMENT '变动前余额',
    balance_after DECIMAL(15,2) NOT NULL COMMENT '变动后余额',
    biz_type VARCHAR(50) COMMENT '业务类型',
    biz_id VARCHAR(100) COMMENT '业务ID',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_biz_id (biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='余额变动记录表';
