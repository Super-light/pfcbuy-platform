-- ========================================
-- 订单服务数据库表
-- Database: pfcbuy_order
-- ========================================

USE pfcbuy_order;

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(64) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_type VARCHAR(20) NOT NULL DEFAULT 'PURCHASE' COMMENT '订单类型(PURCHASE:代购,FORWARD:转运)',
    order_status VARCHAR(50) NOT NULL DEFAULT 'CREATED' COMMENT '订单状态',
    pay_status VARCHAR(50) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态',
    total_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    product_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '商品金额',
    domestic_shipping_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '国内运费',
    international_shipping_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '国际运费',
    service_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '服务费',
    discount_amount DECIMAL(15,2) DEFAULT 0.00 COMMENT '优惠金额',
    currency VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
    exchange_rate DECIMAL(10,4) DEFAULT 1.0000 COMMENT '汇率',
    receiver_name VARCHAR(100) COMMENT '收件人姓名',
    receiver_phone VARCHAR(20) COMMENT '收件人电话',
    receiver_country VARCHAR(50) COMMENT '收件国家',
    receiver_province VARCHAR(50) COMMENT '收件省份',
    receiver_city VARCHAR(50) COMMENT '收件城市',
    receiver_address VARCHAR(500) COMMENT '收件详细地址',
    receiver_zip_code VARCHAR(20) COMMENT '邮编',
    buyer_message VARCHAR(500) COMMENT '买家留言',
    admin_remark VARCHAR(500) COMMENT '管理员备注',
    cancel_reason VARCHAR(500) COMMENT '取消原因',
    payment_id BIGINT COMMENT '支付记录ID',
    payment_time DATETIME COMMENT '支付时间',
    complete_time DATETIME COMMENT '完成时间',
    cancel_time DATETIME COMMENT '取消时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_order_status (order_status),
    INDEX idx_pay_status (pay_status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单商品表
CREATE TABLE IF NOT EXISTS t_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    product_snapshot_id BIGINT NOT NULL COMMENT '商品快照ID',
    platform VARCHAR(50) NOT NULL COMMENT '平台类型',
    platform_product_id VARCHAR(200) NOT NULL COMMENT '平台商品ID',
    product_title VARCHAR(500) NOT NULL COMMENT '商品标题',
    product_image VARCHAR(500) COMMENT '商品图片',
    sku_attributes TEXT COMMENT 'SKU属性',
    unit_price DECIMAL(15,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    subtotal DECIMAL(15,2) NOT NULL COMMENT '小计',
    currency VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
    product_url TEXT COMMENT '商品链接',
    purchase_status VARCHAR(50) DEFAULT 'PENDING' COMMENT '采购状态',
    warehouse_status VARCHAR(50) COMMENT '仓库状态',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_product_snapshot_id (product_snapshot_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单商品表';

-- 订单状态变更日志表
CREATE TABLE IF NOT EXISTS t_order_status_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单号',
    old_status VARCHAR(50) COMMENT '原状态',
    new_status VARCHAR(50) NOT NULL COMMENT '新状态',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(100) COMMENT '操作人姓名',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单状态变更日志表';
