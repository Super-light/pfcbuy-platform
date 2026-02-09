package com.pfcbuy.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 物流线路实体
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_shipping_route")
public class ShippingRoute extends BaseEntity {
    
    /**
     * 线路代码
     */
    private String routeCode;
    
    /**
     * 线路名称
     */
    private String routeName;
    
    /**
     * 物流渠道
     */
    private String channel;
    
    /**
     * 发货国家
     */
    private String originCountry;
    
    /**
     * 目的国家
     */
    private String destinationCountry;
    
    /**
     * 首重（kg）
     */
    private BigDecimal firstWeight;
    
    /**
     * 首重价格
     */
    private BigDecimal firstWeightPrice;
    
    /**
     * 续重（kg）
     */
    private BigDecimal additionalWeight;
    
    /**
     * 续重价格
     */
    private BigDecimal additionalWeightPrice;
    
    /**
     * 最小重量（kg）
     */
    private BigDecimal minWeight;
    
    /**
     * 最大重量（kg）
     */
    private BigDecimal maxWeight;
    
    /**
     * 预计送达天数
     */
    private Integer estimatedDays;
    
    /**
     * 是否支持追踪
     */
    private Boolean trackable;
    
    /**
     * 是否支持保险
     */
    private Boolean insurable;
    
    /**
     * 保险费率（%）
     */
    private BigDecimal insuranceRate;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 备注
     */
    private String remark;
}
