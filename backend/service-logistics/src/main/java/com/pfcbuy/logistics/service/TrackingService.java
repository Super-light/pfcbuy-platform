package com.pfcbuy.logistics.service;

import com.pfcbuy.logistics.dto.TrackingInfoResponse;

import java.util.List;

/**
 * 物流追踪服务接口
 *
 * @author PfcBuy Team
 */
public interface TrackingService {
    
    /**
     * 获取物流追踪信息
     */
    List<TrackingInfoResponse> getTrackingInfo(String trackingNo);
    
    /**
     * 同步物流追踪信息（从物流商API）
     */
    void syncTrackingInfo(String shippingOrderNo);
    
    /**
     * 添加追踪信息（手动）
     */
    void addTrackingInfo(String shippingOrderNo, String status, String description, String location);
}
