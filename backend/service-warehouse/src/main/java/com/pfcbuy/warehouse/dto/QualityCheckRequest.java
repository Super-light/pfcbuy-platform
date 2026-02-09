package com.pfcbuy.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 质检请求
 *
 * @author PfcBuy Team
 */
@Data
public class QualityCheckRequest {
    
    /**
     * 包裹号
     */
    @NotBlank(message = "包裹号不能为空")
    private String packageNo;
    
    /**
     * 质检结果（PASSED/FAILED）
     */
    @NotBlank(message = "质检结果不能为空")
    private String qcResult;
    
    /**
     * 质检备注
     */
    private String qcRemark;
    
    /**
     * 质检照片URL列表
     */
    private List<String> photoUrls;
}
