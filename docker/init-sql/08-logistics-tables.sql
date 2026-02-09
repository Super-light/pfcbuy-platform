-- ========================================
-- 物流服务数据库表
-- Database: pfcbuy_logistics
-- ========================================

USE pfcbuy_logistics;

-- 物流订单表
CREATE TABLE IF NOT EXISTS t_shipping_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    shipping_no VARCHAR(64) NOT NULL UNIQUE COMMENT '物流单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_id BIGINT COMMENT '关联订单ID',
    order_no VARCHAR(64) COMMENT '关联订单号',
    package_ids TEXT COMMENT '包裹ID列表（JSON数组）',
    shipping_type VARCHAR(50) NOT NULL DEFAULT 'INTERNATIONAL' COMMENT '物流类型(DOMESTIC/INTERNATIONAL)',
    route_id BIGINT COMMENT '物流线路ID',
    route_name VARCHAR(200) COMMENT '物流线路名称',
    carrier VARCHAR(100) COMMENT '承运商',
    tracking_no VARCHAR(100) COMMENT '物流跟踪号',
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED' COMMENT '物流状态',
    
    sender_name VARCHAR(100) COMMENT '发件人姓名',
    sender_phone VARCHAR(20) COMMENT '发件人电话',
    sender_country VARCHAR(50) COMMENT '发件国家',
    sender_address VARCHAR(500) COMMENT '发件地址',
    
    receiver_name VARCHAR(100) NOT NULL COMMENT '收件人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收件人电话',
    receiver_country VARCHAR(50) NOT NULL COMMENT '收件国家',
    receiver_province VARCHAR(50) COMMENT '收件省份',
    receiver_city VARCHAR(50) COMMENT '收件城市',
    receiver_district VARCHAR(50) COMMENT '收件区/县',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收件详细地址',
    receiver_zip_code VARCHAR(20) COMMENT '邮编',
    
    weight DECIMAL(10,3) DEFAULT 0.000 COMMENT '总重量（kg）',
    volume_weight DECIMAL(10,3) DEFAULT 0.000 COMMENT '体积重（kg）',
    chargeable_weight DECIMAL(10,3) DEFAULT 0.000 COMMENT '计费重量（kg）',
    
    shipping_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '运费',
    insurance_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '保险费',
    declared_value DECIMAL(15,2) DEFAULT 0.00 COMMENT '申报价值',
    customs_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '关税',
    total_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '总费用',
    currency VARCHAR(10) DEFAULT 'USD' COMMENT '货币类型',
    
    estimated_delivery_days INT COMMENT '预计送达天数',
    shipped_time DATETIME COMMENT '发货时间',
    delivered_time DATETIME COMMENT '签收时间',
    
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    
    INDEX idx_shipping_no (shipping_no),
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_tracking_no (tracking_no),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流订单表';

-- 物流跟踪信息表
CREATE TABLE IF NOT EXISTS t_tracking_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    shipping_id BIGINT NOT NULL COMMENT '物流订单ID',
    shipping_no VARCHAR(64) NOT NULL COMMENT '物流单号',
    tracking_no VARCHAR(100) NOT NULL COMMENT '物流跟踪号',
    status VARCHAR(50) NOT NULL COMMENT '状态',
    description VARCHAR(500) NOT NULL COMMENT '描述',
    location VARCHAR(200) COMMENT '位置',
    occur_time DATETIME NOT NULL COMMENT '发生时间',
    operator VARCHAR(100) COMMENT '操作人',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    
    INDEX idx_shipping_id (shipping_id),
    INDEX idx_shipping_no (shipping_no),
    INDEX idx_tracking_no (tracking_no),
    INDEX idx_occur_time (occur_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流跟踪信息表';

-- 物流线路表
CREATE TABLE IF NOT EXISTS t_shipping_route (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_code VARCHAR(50) NOT NULL UNIQUE COMMENT '线路编码',
    route_name VARCHAR(200) NOT NULL COMMENT '线路名称',
    route_name_en VARCHAR(200) COMMENT '英文名称',
    carrier VARCHAR(100) NOT NULL COMMENT '承运商',
    shipping_type VARCHAR(50) NOT NULL COMMENT '物流类型(DOMESTIC/INTERNATIONAL)',
    origin_country VARCHAR(50) COMMENT '始发国家',
    destination_country VARCHAR(50) COMMENT '目的国家',
    
    min_weight DECIMAL(10,3) DEFAULT 0.000 COMMENT '最小重量（kg）',
    max_weight DECIMAL(10,3) DEFAULT 999.999 COMMENT '最大重量（kg）',
    base_weight DECIMAL(10,3) DEFAULT 0.500 COMMENT '首重（kg）',
    base_price DECIMAL(15,2) DEFAULT 0.00 COMMENT '首重价格',
    additional_weight DECIMAL(10,3) DEFAULT 0.100 COMMENT '续重单位（kg）',
    additional_price DECIMAL(15,2) DEFAULT 0.00 COMMENT '续重价格',
    currency VARCHAR(10) DEFAULT 'USD' COMMENT '货币类型',
    
    estimated_delivery_days INT DEFAULT 7 COMMENT '预计送达天数',
    tracking_available TINYINT DEFAULT 1 COMMENT '是否可跟踪(0:否,1:是)',
    insurance_available TINYINT DEFAULT 1 COMMENT '是否可保价(0:否,1:是)',
    insurance_rate DECIMAL(5,4) DEFAULT 0.0100 COMMENT '保价费率',
    
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
    sort_order INT DEFAULT 0 COMMENT '排序',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    
    INDEX idx_route_code (route_code),
    INDEX idx_carrier (carrier),
    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流线路表';

-- 物流异常记录表
CREATE TABLE IF NOT EXISTS t_logistics_exception (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    shipping_id BIGINT NOT NULL COMMENT '物流订单ID',
    shipping_no VARCHAR(64) NOT NULL COMMENT '物流单号',
    exception_type VARCHAR(50) NOT NULL COMMENT '异常类型(DELAY/LOST/DAMAGE/RETURN/CUSTOMS)',
    exception_desc VARCHAR(500) NOT NULL COMMENT '异常描述',
    exception_time DATETIME NOT NULL COMMENT '异常发生时间',
    handler_id BIGINT COMMENT '处理人ID',
    handler_name VARCHAR(100) COMMENT '处理人姓名',
    handle_result VARCHAR(500) COMMENT '处理结果',
    handle_time DATETIME COMMENT '处理时间',
    status VARCHAR(50) DEFAULT 'PENDING' COMMENT '状态(PENDING/HANDLING/RESOLVED)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_shipping_id (shipping_id),
    INDEX idx_shipping_no (shipping_no),
    INDEX idx_exception_type (exception_type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流异常记录表';
