package com.pfcbuy.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流订单响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingOrderResponse {
    
    private Long id;
    private String shippingOrderNo;
    private Long userId;
    private String packageNo;
    private String orderNo;
    private String channel;
    private String trackingNo;
    private String status;
    private String receiverName;
    private String receiverPhone;
    private String receiverEmail;
    private String receiverCountry;
    private String receiverState;
    private String receiverCity;
    private String receiverAddress;
    private String receiverZipCode;
    private BigDecimal actualWeight;
    private BigDecimal volumeWeight;
    private BigDecimal chargeableWeight;
    private BigDecimal shippingFee;
    private String currency;
    private BigDecimal insuranceFee;
    private BigDecimal customsFee;
    private BigDecimal totalFee;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private LocalDateTime pickupTime;
    private String exceptionReason;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    /**
     * 追踪信息列表
     */
    private List<TrackingInfoResponse> trackingInfos;
}
