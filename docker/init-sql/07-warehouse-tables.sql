-- ========================================
-- 仓储服务数据库表
-- Database: pfcbuy_warehouse
-- ========================================

USE pfcbuy_warehouse;

-- 包裹表
CREATE TABLE IF NOT EXISTS t_package (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    package_no VARCHAR(64) NOT NULL UNIQUE COMMENT '包裹号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_id BIGINT COMMENT '订单ID',
    order_no VARCHAR(64) COMMENT '订单号',
    package_type VARCHAR(50) NOT NULL DEFAULT 'PURCHASE' COMMENT '包裹类型(PURCHASE:代购,FORWARD:转运)',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT '包裹状态',
    warehouse_id BIGINT COMMENT '仓库ID',
    storage_location VARCHAR(100) COMMENT '存储位置',
    weight DECIMAL(10,3) DEFAULT 0.000 COMMENT '实际重量（kg）',
    volume_weight DECIMAL(10,3) DEFAULT 0.000 COMMENT '体积重（kg）',
    length DECIMAL(10,2) DEFAULT 0.00 COMMENT '长度（cm）',
    width DECIMAL(10,2) DEFAULT 0.00 COMMENT '宽度（cm）',
    height DECIMAL(10,2) DEFAULT 0.00 COMMENT '高度（cm）',
    declared_value DECIMAL(15,2) DEFAULT 0.00 COMMENT '申报价值',
    storage_fee DECIMAL(15,2) DEFAULT 0.00 COMMENT '仓储费',
    inbound_time DATETIME COMMENT '入库时间',
    outbound_time DATETIME COMMENT '出库时间',
    qc_status VARCHAR(50) DEFAULT 'PENDING' COMMENT '质检状态',
    qc_time DATETIME COMMENT '质检时间',
    qc_user_id BIGINT COMMENT '质检人员ID',
    qc_result TEXT COMMENT '质检结果（JSON）',
    merged_into_package_id BIGINT COMMENT '合并到的包裹ID',
    is_merged TINYINT DEFAULT 0 COMMENT '是否已合并(0:否,1:是)',
    domestic_tracking_no VARCHAR(100) COMMENT '国内物流单号',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_package_no (package_no),
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status),
    INDEX idx_qc_status (qc_status),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_inbound_time (inbound_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='包裹表';

-- 质检照片表
CREATE TABLE IF NOT EXISTS t_qc_photo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    package_id BIGINT NOT NULL COMMENT '包裹ID',
    photo_type VARCHAR(50) NOT NULL COMMENT '照片类型(ARRIVAL/QC/ISSUE/PACKING)',
    photo_url VARCHAR(500) NOT NULL COMMENT '照片URL',
    thumbnail_url VARCHAR(500) COMMENT '缩略图URL',
    photo_order INT DEFAULT 0 COMMENT '照片顺序',
    description VARCHAR(500) COMMENT '描述',
    upload_user_id BIGINT COMMENT '上传人员ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_package_id (package_id),
    INDEX idx_photo_type (photo_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='质检照片表';

-- 仓库地址表
CREATE TABLE IF NOT EXISTS t_warehouse_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    warehouse_code VARCHAR(50) NOT NULL UNIQUE COMMENT '仓库编码',
    warehouse_name VARCHAR(200) NOT NULL COMMENT '仓库名称',
    country VARCHAR(50) NOT NULL COMMENT '国家',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区/县',
    address VARCHAR(500) NOT NULL COMMENT '详细地址',
    zip_code VARCHAR(20) COMMENT '邮编',
    contact_name VARCHAR(100) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
    capacity INT DEFAULT 0 COMMENT '容量（件）',
    current_stock INT DEFAULT 0 COMMENT '当前库存（件）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除(0:否,1:是)',
    INDEX idx_warehouse_code (warehouse_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库地址表';

-- 包裹操作记录表
CREATE TABLE IF NOT EXISTS t_package_operation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    package_id BIGINT NOT NULL COMMENT '包裹ID',
    package_no VARCHAR(64) NOT NULL COMMENT '包裹号',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型(INBOUND/QC/MERGE/OUTBOUND)',
    operator_id BIGINT COMMENT '操作人员ID',
    operator_name VARCHAR(100) COMMENT '操作人员姓名',
    operation_desc VARCHAR(500) COMMENT '操作描述',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_package_id (package_id),
    INDEX idx_package_no (package_no),
    INDEX idx_operation_type (operation_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='包裹操作记录表';
