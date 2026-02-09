package com.pfcbuy.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pfcbuy.common.enums.OrderStatus;
import com.pfcbuy.common.enums.PaymentStatus;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.common.result.Result;
import com.pfcbuy.order.dto.*;
import com.pfcbuy.order.entity.Order;
import com.pfcbuy.order.entity.OrderItem;
import com.pfcbuy.order.feign.ProductFeignClient;
import com.pfcbuy.order.mapper.OrderItemMapper;
import com.pfcbuy.order.mapper.OrderMapper;
import com.pfcbuy.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Value("${business.order.timeout-minutes:30}")
    private Integer timeoutMinutes;

    @Value("${business.order.order-prefix:PFC}")
    private String orderPrefix;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("开始创建订单，用户ID: {}", request.getUserId());

        // 1. 生成订单号
        String orderNo = generateOrderNo();

        // 2. 创建订单主表
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(request.getUserId());
        order.setOrderStatus(OrderStatus.CREATED.getCode());
        order.setPayStatus(PaymentStatus.UNPAID.getCode());
        order.setAddressId(request.getAddressId());
        order.setUserRemark(request.getUserRemark());
        
        // 设置过期时间
        order.setExpireTime(LocalDateTime.now().plusMinutes(timeoutMinutes));
        
        // TODO: 从地址服务获取收货地址信息
        // 这里暂时使用默认值
        order.setReceiverName("Test User");
        order.setReceiverPhone("1234567890");
        order.setReceiverAddress("Test Address");
        order.setCountry("US");

        // 3. 处理订单项并计算金额
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalProductAmount = BigDecimal.ZERO;

        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            // 调用商品服务获取商品快照
            ProductSnapshotDTO snapshot = getProductSnapshot(itemRequest.getProductSnapshotId());
            
            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setProductSnapshotId(itemRequest.getProductSnapshotId());
            orderItem.setPlatform(snapshot.getPlatform());
            orderItem.setPlatformProductId(snapshot.getPlatformProductId());
            orderItem.setPlatformSkuId(snapshot.getPlatformSkuId());
            orderItem.setTitle(snapshot.getTitle());
            orderItem.setSkuAttributes(snapshot.getSkuAttributes());
            orderItem.setPrice(snapshot.getPrice());
            orderItem.setCurrency(snapshot.getCurrency());
            orderItem.setQuantity(itemRequest.getQuantity());
            
            // 计算小计
            BigDecimal subtotal = snapshot.getPrice().multiply(new BigDecimal(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);
            
            orderItem.setImageUrl(snapshot.getMainImage());
            orderItem.setProductUrl(snapshot.getProductUrl());
            orderItem.setStatus("NORMAL");
            orderItem.setRemark(itemRequest.getRemark());
            
            orderItems.add(orderItem);
            
            // 累加商品总额
            totalProductAmount = totalProductAmount.add(subtotal);
        }

        // 4. 计算订单金额
        order.setProductAmount(totalProductAmount);
        order.setOriginalCurrency("CNY"); // 假设商品都是CNY
        
        // TODO: 计算服务费和运费
        BigDecimal serviceFee = totalProductAmount.multiply(new BigDecimal("0.05")); // 5%服务费
        BigDecimal shippingFee = new BigDecimal("50.00"); // 固定运费
        
        order.setServiceFee(serviceFee);
        order.setShippingFee(shippingFee);
        
        BigDecimal totalAmount = totalProductAmount.add(serviceFee).add(shippingFee);
        order.setTotalAmount(totalAmount);
        order.setPaidAmount(BigDecimal.ZERO);
        
        // TODO: 根据用户设置的币种进行汇率转换
        order.setCurrency("USD");
        order.setExchangeRate(new BigDecimal("0.14")); // 假设汇率

        // 5. 保存订单
        orderMapper.insert(order);
        log.info("订单主表保存成功，订单ID: {}, 订单号: {}", order.getId(), orderNo);

        // 6. 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemMapper.batchInsert(orderItems);
        log.info("订单项保存成功，数量: {}", orderItems.size());

        // 7. 返回订单响应
        return convertToOrderResponse(order, orderItems);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        return convertToOrderResponse(order, items);
    }

    @Override
    public OrderResponse getOrderByOrderNo(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        return convertToOrderResponse(order, items);
    }

    @Override
    public Page<OrderResponse> queryOrders(OrderQueryRequest request) {
        Page<Order> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(request.getUserId() != null, Order::getUserId, request.getUserId())
                   .eq(StringUtils.hasText(request.getOrderNo()), Order::getOrderNo, request.getOrderNo())
                   .eq(StringUtils.hasText(request.getOrderStatus()), Order::getOrderStatus, request.getOrderStatus())
                   .eq(StringUtils.hasText(request.getPayStatus()), Order::getPayStatus, request.getPayStatus())
                   .orderByDesc(Order::getCreateTime);
        
        Page<Order> orderPage = orderMapper.selectPage(page, queryWrapper);
        
        // 转换为响应对象
        Page<OrderResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(orderPage, responsePage, "records");
        
        List<OrderResponse> responseList = orderPage.getRecords().stream()
                .map(order -> {
                    List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
                    return convertToOrderResponse(order, items);
                })
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        return responsePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 只有未支付或已创建的订单才能取消
        if (!OrderStatus.CREATED.getCode().equals(order.getOrderStatus()) 
            && !PaymentStatus.UNPAID.getCode().equals(order.getPayStatus())) {
            throw new BusinessException("订单状态不允许取消");
        }
        
        order.setOrderStatus(OrderStatus.CANCELLED.getCode());
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason(reason);
        
        return orderMapper.updateById(order) > 0;
    }

    @Override
    public boolean updateOrderStatus(Long orderId, String orderStatus) {
        return orderMapper.updateOrderStatus(orderId, orderStatus) > 0;
    }

    @Override
    public boolean updatePayStatus(Long orderId, String payStatus) {
        return orderMapper.updatePayStatus(orderId, payStatus) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean paymentSuccess(String orderNo, String paymentId) {
        log.info("处理支付成功回调，订单号: {}, 支付ID: {}", orderNo, paymentId);
        
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 更新订单状态
        order.setPayStatus(PaymentStatus.PAID.getCode());
        order.setOrderStatus(OrderStatus.PAID.getCode());
        order.setPayTime(LocalDateTime.now());
        order.setPaidAmount(order.getTotalAmount());
        
        return orderMapper.updateById(order) > 0;
    }

    /**
     * 生成订单号
     * 格式: PFC + yyyyMMddHHmmss + 6位随机数
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%06d", (int) (Math.random() * 1000000));
        return orderPrefix + timestamp + random;
    }

    /**
     * 获取商品快照
     */
    private ProductSnapshotDTO getProductSnapshot(Long snapshotId) {
        try {
            Result<ProductSnapshotDTO> result = productFeignClient.getProductSnapshot(snapshotId);
            if (result.isSuccess() && result.getData() != null) {
                return result.getData();
            }
            throw new BusinessException("商品快照不存在: " + snapshotId);
        } catch (Exception e) {
            log.error("获取商品快照失败: {}", snapshotId, e);
            throw new BusinessException("获取商品信息失败");
        }
    }

    /**
     * 转换为订单响应对象
     */
    private OrderResponse convertToOrderResponse(Order order, List<OrderItem> items) {
        OrderResponse response = new OrderResponse();
        BeanUtils.copyProperties(order, response);
        
        // 设置状态描述
        response.setOrderStatusDesc(OrderStatus.getDescByCode(order.getOrderStatus()));
        response.setPayStatusDesc(PaymentStatus.getDescByCode(order.getPayStatus()));
        
        // 转换订单项
        if (items != null && !items.isEmpty()) {
            List<OrderResponse.OrderItemResponse> itemResponses = items.stream()
                    .map(item -> {
                        OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
                        BeanUtils.copyProperties(item, itemResponse);
                        return itemResponse;
                    })
                    .collect(Collectors.toList());
            response.setItems(itemResponses);
        }
        
        return response;
    }
}
