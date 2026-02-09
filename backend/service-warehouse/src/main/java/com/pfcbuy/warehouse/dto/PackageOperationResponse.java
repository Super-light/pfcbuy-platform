package com.pfcbuy.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 包裹操作记录响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageOperationResponse {
    
    private Long id;
    private String packageNo;
    private String operationType;
    private String operationTypeName;
    private String beforeStatus;
    private String afterStatus;
    private Long operatorId;
    private String operatorName;
    private String description;
    private String remark;
    private LocalDateTime createTime;
}
