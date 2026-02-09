package com.pfcbuy.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfcbuy.order.dto.CreateOrderRequest;
import com.pfcbuy.order.dto.OrderQueryRequest;
import com.pfcbuy.order.dto.OrderResponse;
import com.pfcbuy.order.entity.Order;

/**
 * 订单服务接口
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param request 创建订单请求
     * @return 订单响应
     */
    OrderResponse createOrder(CreateOrderRequest request);

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId 订单ID
     * @return 订单响应
     */
    OrderResponse getOrderById(Long orderId);

    /**
     * 根据订单号查询订单详情
     *
     * @param orderNo 订单号
     * @return 订单响应
     */
    OrderResponse getOrderByOrderNo(String orderNo);

    /**
     * 分页查询订单列表
     *
     * @param request 查询请求
     * @return 订单分页
     */
    Page<OrderResponse> queryOrders(OrderQueryRequest request);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param reason 取消原因
     * @return 是否成功
     */
    boolean cancelOrder(Long orderId, String reason);

    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @return 是否成功
     */
    boolean updateOrderStatus(Long orderId, String orderStatus);

    /**
     * 更新支付状态
     *
     * @param orderId 订单ID
     * @param payStatus 支付状态
     * @return 是否成功
     */
    boolean updatePayStatus(Long orderId, String payStatus);

    /**
     * 支付成功回调
     *
     * @param orderNo 订单号
     * @param paymentId 支付ID
     * @return 是否成功
     */
    boolean paymentSuccess(String orderNo, String paymentId);
}
