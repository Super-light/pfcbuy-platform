package com.pfcbuy.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 包裹响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {
    
    private Long id;
    private String packageNo;
    private Long userId;
    private String orderNo;
    private String status;
    private String statusName;
    private String expressCompany;
    private String trackingNo;
    private String senderName;
    private String senderPhone;
    private String productDescription;
    private Integer quantity;
    private BigDecimal declaredValue;
    private String currency;
    private BigDecimal actualWeight;
    private BigDecimal volumeWeight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private LocalDateTime inboundTime;
    private LocalDateTime outboundTime;
    private Integer storageDays;
    private BigDecimal storageFee;
    private Boolean needQc;
    private String qcStatus;
    private String qcRemark;
    private LocalDateTime qcTime;
    private String parentPackageNo;
    private Boolean merged;
    private String exceptionReason;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    /**
     * 质检照片列表
     */
    private List<QcPhotoResponse> qcPhotos;
    
    /**
     * 操作记录列表
     */
    private List<PackageOperationResponse> operations;
}
