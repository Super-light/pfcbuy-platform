package com.pfcbuy.logistics.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 运费试算请求
 *
 * @author PfcBuy Team
 */
@Data
public class ShippingFeeRequest {
    
    @NotBlank(message = "目的国家不能为空")
    private String destinationCountry;
    
    private String destinationState;
    
    private String destinationCity;
    
    @NotNull(message = "实际重量不能为空")
    @DecimalMin(value = "0.01", message = "实际重量必须大于0")
    private BigDecimal actualWeight;
    
    /**
     * 长（cm）
     */
    private BigDecimal length;
    
    /**
     * 宽（cm）
     */
    private BigDecimal width;
    
    /**
     * 高（cm）
     */
    private BigDecimal height;
    
    /**
     * 物流渠道（可选，为空则返回所有可用渠道）
     */
    private String channel;
    
    /**
     * 是否需要保险
     */
    private Boolean needInsurance = false;
    
    /**
     * 保险金额
     */
    private BigDecimal insuranceAmount;
}
