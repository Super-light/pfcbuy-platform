package com.pfcbuy.logistics.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建物流订单请求
 *
 * @author PfcBuy Team
 */
@Data
public class CreateShippingRequest {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "包裹号不能为空")
    private String packageNo;
    
    @NotBlank(message = "物流渠道不能为空")
    private String channel;
    
    @NotBlank(message = "收件人姓名不能为空")
    private String receiverName;
    
    @NotBlank(message = "收件人电话不能为空")
    private String receiverPhone;
    
    @Email(message = "收件人邮箱格式不正确")
    private String receiverEmail;
    
    @NotBlank(message = "收件国家不能为空")
    private String receiverCountry;
    
    private String receiverState;
    
    @NotBlank(message = "收件城市不能为空")
    private String receiverCity;
    
    @NotBlank(message = "收件地址不能为空")
    private String receiverAddress;
    
    @NotBlank(message = "收件邮编不能为空")
    private String receiverZipCode;
    
    @NotNull(message = "实际重量不能为空")
    @DecimalMin(value = "0.01", message = "实际重量必须大于0")
    private BigDecimal actualWeight;
    
    private BigDecimal volumeWeight;
    
    /**
     * 是否需要保险
     */
    private Boolean needInsurance = false;
    
    /**
     * 保险金额
     */
    private BigDecimal insuranceAmount;
    
    private String remark;
}
