package com.pfcbuy.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物流订单实体
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_shipping_order")
public class ShippingOrder extends BaseEntity {
    
    /**
     * 物流单号
     */
    private String shippingNo;

    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 关联订单号
     */
    private String orderNo;
    
    /**
     * 包裹ID列表（JSON数组）
     */
    private String packageIds;

    /**
     * 物流类型(DOMESTIC/INTERNATIONAL)
     */
    private String shippingType;

    /**
     * 物流线路ID
     */
    private Long routeId;

    /**
     * 物流线路名称
     */
    private String routeName;

    /**
     * 承运商
     */
    private String carrier;

    /**
     * 物流单号
     */
    private String trackingNo;
    
    /**
     * 物流状态
     */
    private String status;
    
    /**
     * 发件人姓名
     */
    private String senderName;

    /**
     * 发件人电话
     */
    private String senderPhone;

    /**
     * 发件国家
     */
    private String senderCountry;

    /**
     * 发件地址
     */
    private String senderAddress;

    /**
     * 收件人姓名
     */
    private String receiverName;
    
    /**
     * 收件人电话
     */
    private String receiverPhone;
    
    /**
     * 收件国家
     */
    private String receiverCountry;
    
    /**
     * 收件省份
     */
    private String receiverProvince;

    /**
     * 收件城市
     */
    private String receiverCity;
    
    /**
     * 收件区/县
     */
    private String receiverDistrict;

    /**
     * 收件详细地址
     */
    private String receiverAddress;
    
    /**
     * 邮编
     */
    private String receiverZipCode;
    
    /**
     * 总重量（kg）
     */
    private BigDecimal weight;

    /**
     * 体积重（kg）
     */
    private BigDecimal volumeWeight;
    
    /**
     * 计费重量（kg）
     */
    private BigDecimal chargeableWeight;
    
    /**
     * 运费
     */
    private BigDecimal shippingFee;
    
    /**
     * 保险费
     */
    private BigDecimal insuranceFee;

    /**
     * 申报价值
     */
    private BigDecimal declaredValue;

    /**
     * 关税
     */
    private BigDecimal customsFee;
    
    /**
     * 总费用
     */
    private BigDecimal totalFee;
    
    /**
     * 货币类型
     */
    private String currency;

    /**
     * 预计送达天数
     */
    private Integer estimatedDeliveryDays;

    /**
     * 发货时间
     */
    private LocalDateTime shippedTime;

    /**
     * 签收时间
     */
    private LocalDateTime deliveredTime;

    /**
     * 备注
     */
    private String remark;
}
