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
     * 英文名称
     */
    private String routeNameEn;

    /**
     * 承运商
     */
    private String carrier;

    /**
     * 物流类型(DOMESTIC/INTERNATIONAL)
     */
    private String shippingType;

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
    private BigDecimal baseWeight;

    /**
     * 首重价格
     */
    private BigDecimal basePrice;

    /**
     * 续重（kg）
     */
    private BigDecimal additionalWeight;
    
    /**
     * 续重价格
     */
    private BigDecimal additionalPrice;

    /**
     * 最小重量（kg）
     */
    private BigDecimal minWeight;
    
    /**
     * 最大重量（kg）
     */
    private BigDecimal maxWeight;
    
    /**
     * 货币类型
     */
    private String currency;

    /**
     * 预计送达天数
     */
    private Integer estimatedDeliveryDays;

    /**
     * 是否可跟踪(0:否,1:是)
     */
    private Boolean trackingAvailable;

    /**
     * 是否可保价(0:否,1:是)
     */
    private Boolean insuranceAvailable;

    /**
     * 保价费率
     */
    private BigDecimal insuranceRate;
    
    /**
     * 状态(ACTIVE/INACTIVE)
     */
    private String status;

    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 备注
     */
    private String remark;
}
