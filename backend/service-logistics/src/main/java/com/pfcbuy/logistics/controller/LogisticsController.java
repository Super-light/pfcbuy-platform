package com.pfcbuy.logistics.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.logistics.dto.*;
import com.pfcbuy.logistics.service.LogisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物流Controller
 *
 * @author PfcBuy Team
 */
@Slf4j
@RestController
@RequestMapping("/api/logistics")
@RequiredArgsConstructor
public class LogisticsController {
    
    private final LogisticsService logisticsService;
    
    /**
     * 创建物流订单
     */
    @PostMapping("/orders")
    public Result<ShippingOrderResponse> createShippingOrder(@Valid @RequestBody CreateShippingRequest request) {
        ShippingOrderResponse response = logisticsService.createShippingOrder(request);
        return Result.success(response);
    }
    
    /**
     * 获取物流订单详情
     */
    @GetMapping("/orders/{shippingOrderNo}")
    public Result<ShippingOrderResponse> getShippingOrder(@PathVariable String shippingOrderNo) {
        ShippingOrderResponse response = logisticsService.getShippingOrder(shippingOrderNo);
        return Result.success(response);
    }
    
    /**
     * 根据包裹号获取物流订单
     */
    @GetMapping("/orders/package/{packageNo}")
    public Result<ShippingOrderResponse> getShippingOrderByPackageNo(@PathVariable String packageNo) {
        ShippingOrderResponse response = logisticsService.getShippingOrderByPackageNo(packageNo);
        return Result.success(response);
    }
    
    /**
     * 获取用户的物流订单列表
     */
    @GetMapping("/orders/user/{userId}")
    public Result<List<ShippingOrderResponse>> getUserShippingOrders(@PathVariable Long userId) {
        List<ShippingOrderResponse> orders = logisticsService.getUserShippingOrders(userId);
        return Result.success(orders);
    }
    
    /**
     * 取消物流订单
     */
    @PutMapping("/orders/{shippingOrderNo}/cancel")
    public Result<Void> cancelShippingOrder(@PathVariable String shippingOrderNo) {
        logisticsService.cancelShippingOrder(shippingOrderNo);
        return Result.success(null);
    }
    
    /**
     * 运费试算
     */
    @PostMapping("/fee/calculate")
    public Result<ShippingFeeResponse> calculateShippingFee(@Valid @RequestBody ShippingFeeRequest request) {
        ShippingFeeResponse response = logisticsService.calculateShippingFee(request);
        return Result.success(response);
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Logistics Service is running");
    }
}
