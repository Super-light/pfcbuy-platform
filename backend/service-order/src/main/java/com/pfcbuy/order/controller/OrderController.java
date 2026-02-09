package com.pfcbuy.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfcbuy.common.result.Result;
import com.pfcbuy.order.dto.CreateOrderRequest;
import com.pfcbuy.order.dto.OrderQueryRequest;
import com.pfcbuy.order.dto.OrderResponse;
import com.pfcbuy.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单Controller
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public Result<OrderResponse> createOrder(@RequestBody @Validated CreateOrderRequest request) {
        log.info("接收创建订单请求: {}", request);
        OrderResponse response = orderService.createOrder(request);
        return Result.success(response);
    }

    /**
     * 根据订单ID查询订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderResponse> getOrderById(@PathVariable Long orderId) {
        log.info("查询订单详情，订单ID: {}", orderId);
        OrderResponse response = orderService.getOrderById(orderId);
        return Result.success(response);
    }

    /**
     * 根据订单号查询订单详情
     */
    @GetMapping("/no/{orderNo}")
    public Result<OrderResponse> getOrderByOrderNo(@PathVariable String orderNo) {
        log.info("查询订单详情，订单号: {}", orderNo);
        OrderResponse response = orderService.getOrderByOrderNo(orderNo);
        return Result.success(response);
    }

    /**
     * 分页查询订单列表
     */
    @GetMapping("/list")
    public Result<Page<OrderResponse>> queryOrders(OrderQueryRequest request) {
        log.info("分页查询订单列表: {}", request);
        Page<OrderResponse> page = orderService.queryOrders(request);
        return Result.success(page);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public Result<Boolean> cancelOrder(@PathVariable Long orderId, 
                                       @RequestParam(required = false) String reason) {
        log.info("取消订单，订单ID: {}, 原因: {}", orderId, reason);
        boolean success = orderService.cancelOrder(orderId, reason);
        return Result.success(success);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{orderId}/status")
    public Result<Boolean> updateOrderStatus(@PathVariable Long orderId, 
                                             @RequestParam String orderStatus) {
        log.info("更新订单状态，订单ID: {}, 状态: {}", orderId, orderStatus);
        boolean success = orderService.updateOrderStatus(orderId, orderStatus);
        return Result.success(success);
    }

    /**
     * 支付成功回调（内部调用）
     */
    @PostMapping("/payment-callback")
    public Result<Boolean> paymentCallback(@RequestParam String orderNo, 
                                           @RequestParam String paymentId) {
        log.info("接收支付成功回调，订单号: {}, 支付ID: {}", orderNo, paymentId);
        boolean success = orderService.paymentSuccess(orderNo, paymentId);
        return Result.success(success);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Order Service is running");
    }
}
