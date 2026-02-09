package com.pfcbuy.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 包裹操作记录
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_package_operation")
public class PackageOperation extends BaseEntity {
    
    /**
     * 包裹号
     */
    private String packageNo;
    
    /**
     * 操作类型（INBOUND/QC/MERGE/SPLIT/OUTBOUND/EXCEPTION）
     */
    private String operationType;
    
    /**
     * 操作前状态
     */
    private String beforeStatus;
    
    /**
     * 操作后状态
     */
    private String afterStatus;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人姓名
     */
    private String operatorName;
    
    /**
     * 操作描述
     */
    private String description;
    
    /**
     * 备注
     */
    private String remark;
}
