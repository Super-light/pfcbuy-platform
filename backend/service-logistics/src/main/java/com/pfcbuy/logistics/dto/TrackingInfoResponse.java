package com.pfcbuy.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物流追踪信息响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingInfoResponse {
    
    private Long id;
    private String shippingOrderNo;
    private String trackingNo;
    private String channel;
    private String status;
    private String description;
    private String location;
    private String country;
    private String city;
    private LocalDateTime eventTime;
    private String operator;
    private String remark;
}
