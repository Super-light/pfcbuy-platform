package com.pfcbuy.warehouse.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 包裹合并请求
 *
 * @author PfcBuy Team
 */
@Data
public class PackageMergeRequest {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 要合并的包裹号列表
     */
    @NotEmpty(message = "合并的包裹列表不能为空")
    private List<String> packageNos;
    
    /**
     * 合并备注
     */
    private String remark;
}
