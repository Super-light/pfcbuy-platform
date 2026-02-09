package com.pfcbuy.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 包裹入库请求
 *
 * @author PfcBuy Team
 */
@Data
public class PackageInboundRequest {
    
    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 快递公司
     */
    @NotBlank(message = "快递公司不能为空")
    private String expressCompany;
    
    /**
     * 物流单号
     */
    @NotBlank(message = "物流单号不能为空")
    private String trackingNo;
    
    /**
     * 发件人姓名
     */
    private String senderName;
    
    /**
     * 发件人电话
     */
    private String senderPhone;
    
    /**
     * 商品描述
     */
    private String productDescription;
    
    /**
     * 商品数量
     */
    private Integer quantity;
    
    /**
     * 申报价值
     */
    private BigDecimal declaredValue;
    
    /**
     * 币种
     */
    private String currency = "CNY";
    
    /**
     * 实际重量（kg）
     */
    private BigDecimal actualWeight;
    
    /**
     * 长度（cm）
     */
    private BigDecimal length;
    
    /**
     * 宽度（cm）
     */
    private BigDecimal width;
    
    /**
     * 高度（cm）
     */
    private BigDecimal height;
    
    /**
     * 是否需要质检
     */
    private Boolean needQc = true;
    
    /**
     * 备注
     */
    private String remark;
}
