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
     * 物流订单号
     */
    private String shippingOrderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 包裹号
     */
    private String packageNo;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 物流渠道（DHL/FEDEX/UPS/EMS）
     */
    private String channel;
    
    /**
     * 物流单号
     */
    private String trackingNo;
    
    /**
     * 状态（CREATED/PICKED_UP/IN_TRANSIT/OUT_FOR_DELIVERY/DELIVERED/EXCEPTION）
     */
    private String status;
    
    /**
     * 收件人姓名
     */
    private String receiverName;
    
    /**
     * 收件人电话
     */
    private String receiverPhone;
    
    /**
     * 收件人邮箱
     */
    private String receiverEmail;
    
    /**
     * 收件国家
     */
    private String receiverCountry;
    
    /**
     * 收件省/州
     */
    private String receiverState;
    
    /**
     * 收件城市
     */
    private String receiverCity;
    
    /**
     * 收件地址
     */
    private String receiverAddress;
    
    /**
     * 收件邮编
     */
    private String receiverZipCode;
    
    /**
     * 实际重量（kg）
     */
    private BigDecimal actualWeight;
    
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
     * 运费币种
     */
    private String currency;
    
    /**
     * 保险费
     */
    private BigDecimal insuranceFee;
    
    /**
     * 关税
     */
    private BigDecimal customsFee;
    
    /**
     * 总费用
     */
    private BigDecimal totalFee;
    
    /**
     * 预计送达时间
     */
    private LocalDateTime estimatedDeliveryTime;
    
    /**
     * 实际送达时间
     */
    private LocalDateTime actualDeliveryTime;
    
    /**
     * 揽件时间
     */
    private LocalDateTime pickupTime;
    
    /**
     * 异常原因
     */
    private String exceptionReason;
    
    /**
     * 备注
     */
    private String remark;
}
