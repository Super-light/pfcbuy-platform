package com.pfcbuy.logistics.service;

import com.pfcbuy.logistics.dto.*;

import java.util.List;

/**
 * 物流服务接口
 *
 * @author PfcBuy Team
 */
public interface LogisticsService {
    
    /**
     * 创建物流订单
     */
    ShippingOrderResponse createShippingOrder(CreateShippingRequest request);
    
    /**
     * 获取物流订单详情
     */
    ShippingOrderResponse getShippingOrder(String shippingOrderNo);
    
    /**
     * 根据包裹号获取物流订单
     */
    ShippingOrderResponse getShippingOrderByPackageNo(String packageNo);
    
    /**
     * 获取用户的物流订单列表
     */
    List<ShippingOrderResponse> getUserShippingOrders(Long userId);
    
    /**
     * 更新物流状态
     */
    void updateShippingStatus(String shippingOrderNo, String status);
    
    /**
     * 取消物流订单
     */
    void cancelShippingOrder(String shippingOrderNo);
    
    /**
     * 运费试算
     */
    ShippingFeeResponse calculateShippingFee(ShippingFeeRequest request);
}
