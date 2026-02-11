package com.pfcbuy.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.logistics.dto.*;
import com.pfcbuy.logistics.entity.ShippingOrder;
import com.pfcbuy.logistics.entity.ShippingRoute;
import com.pfcbuy.logistics.mapper.ShippingOrderMapper;
import com.pfcbuy.logistics.mapper.ShippingRouteMapper;
import com.pfcbuy.logistics.mapper.TrackingInfoMapper;
import com.pfcbuy.logistics.service.LogisticsService;
import com.pfcbuy.logistics.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 物流服务实现
 *
 * @author PfcBuy Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {
    
    private final ShippingOrderMapper shippingOrderMapper;
    private final ShippingRouteMapper shippingRouteMapper;
    private final TrackingInfoMapper trackingInfoMapper;
    private final TrackingService trackingService;
    
    /**
     * 体积重系数（默认5000）
     */
    private static final BigDecimal VOLUME_DIVISOR = new BigDecimal("5000");
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShippingOrderResponse createShippingOrder(CreateShippingRequest request) {
        log.info("创建物流订单，用户ID: {}, 包裹号: {}", request.getUserId(), request.getPackageNo());
        
        // 1. 验证包裹号是否已存在物流订单
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getPackageIds, request.getPackageNo());
        if (shippingOrderMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该包裹已存在物流订单");
        }
        
        // 2. 计算体积重
        BigDecimal volumeWeight = request.getVolumeWeight();
        if (volumeWeight == null || volumeWeight.compareTo(BigDecimal.ZERO) <= 0) {
            volumeWeight = request.getActualWeight(); // 如果没提供体积重，使用实际重量
        }
        
        // 3. 计算计费重量（取实际重量和体积重的较大值）
        BigDecimal chargeableWeight = request.getActualWeight().max(volumeWeight);
        
        // 4. 获取物流线路并计算运费
        ShippingRoute route = getShippingRoute(request.getChannel(), request.getReceiverCountry());
        if (route == null || !"ENABLED".equals(route.getStatus())) {
            throw new BusinessException("该物流渠道暂不可用");
        }
        
        BigDecimal shippingFee = calculateFee(chargeableWeight, route);
        
        // 5. 计算保险费
        BigDecimal insuranceFee = BigDecimal.ZERO;
        if (Boolean.TRUE.equals(request.getNeedInsurance()) && request.getInsuranceAmount() != null) {
            insuranceFee = request.getInsuranceAmount()
                    .multiply(route.getInsuranceRate())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        // 6. 创建物流订单
        ShippingOrder shippingOrder = ShippingOrder.builder()
                .shippingNo(generateShippingOrderNo())
                .userId(request.getUserId())
                .packageIds(request.getPackageNo())
                .carrier(request.getChannel())
                .trackingNo(generateTrackingNo(request.getChannel()))
                .status("CREATED")
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .receiverCountry(request.getReceiverCountry())
                .receiverProvince(request.getReceiverState())
                .receiverCity(request.getReceiverCity())
                .receiverAddress(request.getReceiverAddress())
                .receiverZipCode(request.getReceiverZipCode())
                .weight(request.getActualWeight())
                .volumeWeight(volumeWeight)
                .chargeableWeight(chargeableWeight)
                .shippingFee(shippingFee)
                .currency("USD")
                .insuranceFee(insuranceFee)
                .customsFee(BigDecimal.ZERO)
                .totalFee(shippingFee.add(insuranceFee))
                .estimatedDeliveryDays(route.getEstimatedDeliveryDays())
                .remark(request.getRemark())
                .build();
        
        shippingOrderMapper.insert(shippingOrder);
        
        // 7. 创建初始追踪信息
        trackingService.addTrackingInfo(
                shippingOrder.getShippingNo(),
                "CREATED",
                "物流订单已创建",
                "China"
        );
        
        log.info("物流订单创建成功，订单号: {}", shippingOrder.getShippingNo());

        return convertToResponse(shippingOrder);
    }
    
    @Override
    public ShippingOrderResponse getShippingOrder(String shippingOrderNo) {
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getShippingNo, shippingOrderNo);
        ShippingOrder shippingOrder = shippingOrderMapper.selectOne(wrapper);
        
        if (shippingOrder == null) {
            throw new BusinessException("物流订单不存在");
        }
        
        return convertToResponse(shippingOrder);
    }
    
    @Override
    public ShippingOrderResponse getShippingOrderByPackageNo(String packageNo) {
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getPackageIds, packageNo);
        ShippingOrder shippingOrder = shippingOrderMapper.selectOne(wrapper);
        
        if (shippingOrder == null) {
            throw new BusinessException("该包裹没有物流订单");
        }
        
        return convertToResponse(shippingOrder);
    }
    
    @Override
    public List<ShippingOrderResponse> getUserShippingOrders(Long userId) {
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getUserId, userId)
                .orderByDesc(ShippingOrder::getCreateTime);
        
        List<ShippingOrder> orders = shippingOrderMapper.selectList(wrapper);
        
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShippingStatus(String shippingOrderNo, String status) {
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getShippingNo, shippingOrderNo);
        ShippingOrder shippingOrder = shippingOrderMapper.selectOne(wrapper);
        
        if (shippingOrder == null) {
            throw new BusinessException("物流订单不存在");
        }
        
        shippingOrder.setStatus(status);
        if ("DELIVERED".equals(status)) {
            shippingOrder.setDeliveredTime(LocalDateTime.now());
        }
        
        shippingOrderMapper.updateById(shippingOrder);
        
        log.info("物流订单状态已更新，订单号: {}, 新状态: {}", shippingOrderNo, status);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelShippingOrder(String shippingOrderNo) {
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getShippingNo, shippingOrderNo);
        ShippingOrder shippingOrder = shippingOrderMapper.selectOne(wrapper);
        
        if (shippingOrder == null) {
            throw new BusinessException("物流订单不存在");
        }
        
        if (!"CREATED".equals(shippingOrder.getStatus())) {
            throw new BusinessException("只能取消未发货的订单");
        }
        
        shippingOrder.setStatus("CANCELLED");
        shippingOrderMapper.updateById(shippingOrder);
        
        trackingService.addTrackingInfo(shippingOrderNo, "CANCELLED", "订单已取消", null);
        
        log.info("物流订单已取消，订单号: {}", shippingOrderNo);
    }
    
    @Override
    public ShippingFeeResponse calculateShippingFee(ShippingFeeRequest request) {
        // 1. 计算体积重
        BigDecimal volumeWeight = BigDecimal.ZERO;
        if (request.getLength() != null && request.getWidth() != null && request.getHeight() != null) {
            volumeWeight = request.getLength()
                    .multiply(request.getWidth())
                    .multiply(request.getHeight())
                    .divide(VOLUME_DIVISOR, 2, RoundingMode.HALF_UP);
        }
        
        // 2. 计算计费重量
        BigDecimal chargeableWeight = request.getActualWeight().max(volumeWeight);
        
        // 3. 获取所有可用线路
        LambdaQueryWrapper<ShippingRoute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingRoute::getDestinationCountry, request.getDestinationCountry())
                .eq(ShippingRoute::getStatus, "ENABLED")
                .orderByAsc(ShippingRoute::getSortOrder);
        
        if (request.getChannel() != null && !request.getChannel().isEmpty()) {
            wrapper.eq(ShippingRoute::getCarrier, request.getChannel());
        }
        
        List<ShippingRoute> routes = shippingRouteMapper.selectList(wrapper);
        
        // 4. 计算每条线路的费用
        List<ShippingFeeResponse.RouteOption> routeOptions = routes.stream()
                .map(route -> {
                    BigDecimal shippingFee = calculateFee(chargeableWeight, route);
                    
                    // 计算保险费
                    BigDecimal insuranceFee = BigDecimal.ZERO;
                    if (Boolean.TRUE.equals(request.getNeedInsurance()) 
                            && request.getInsuranceAmount() != null
                            && Boolean.TRUE.equals(route.getInsuranceAvailable())) {
                        insuranceFee = request.getInsuranceAmount()
                                .multiply(route.getInsuranceRate())
                                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    }
                    
                    return ShippingFeeResponse.RouteOption.builder()
                            .channel(route.getCarrier())
                            .routeName(route.getRouteName())
                            .chargeableWeight(chargeableWeight)
                            .shippingFee(shippingFee)
                            .insuranceFee(insuranceFee)
                            .totalFee(shippingFee.add(insuranceFee))
                            .currency("USD")
                            .estimatedDays(route.getEstimatedDeliveryDays())
                            .trackable(route.getTrackingAvailable())
                            .remark(route.getRemark())
                            .build();
                })
                .collect(Collectors.toList());
        
        return ShippingFeeResponse.builder()
                .routes(routeOptions)
                .build();
    }
    
    /**
     * 获取物流线路
     */
    private ShippingRoute getShippingRoute(String channel, String destinationCountry) {
        LambdaQueryWrapper<ShippingRoute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingRoute::getCarrier, channel)
                .eq(ShippingRoute::getDestinationCountry, destinationCountry)
                .eq(ShippingRoute::getStatus, "ENABLED");

        return shippingRouteMapper.selectOne(wrapper);
    }
    
    /**
     * 计算运费
     */
    private BigDecimal calculateFee(BigDecimal chargeableWeight, ShippingRoute route) {
        BigDecimal fee;
        
        if (chargeableWeight.compareTo(route.getBaseWeight()) <= 0) {
            // 首重以内
            fee = route.getBasePrice();
        } else {
            // 首重 + 续重
            BigDecimal additionalWeight = chargeableWeight.subtract(route.getBaseWeight());
            BigDecimal additionalWeightUnits = additionalWeight
                    .divide(route.getAdditionalWeight(), 0, RoundingMode.UP);
            BigDecimal additionalFee = additionalWeightUnits.multiply(route.getAdditionalPrice());
            fee = route.getBasePrice().add(additionalFee);
        }
        
        return fee.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 生成物流订单号
     */
    private String generateShippingOrderNo() {
        return "SH" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    /**
     * 生成物流单号
     */
    private String generateTrackingNo(String channel) {
        return channel.toUpperCase() + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    /**
     * 转换为响应对象
     */
    private ShippingOrderResponse convertToResponse(ShippingOrder shippingOrder) {
        ShippingOrderResponse response = new ShippingOrderResponse();

        response.setId(shippingOrder.getId());
        response.setShippingOrderNo(shippingOrder.getShippingNo());
        response.setUserId(shippingOrder.getUserId());
        response.setPackageNo(shippingOrder.getPackageIds());
        response.setOrderNo(shippingOrder.getOrderNo());
        response.setChannel(shippingOrder.getCarrier());
        response.setTrackingNo(shippingOrder.getTrackingNo());
        response.setStatus(shippingOrder.getStatus());
        response.setReceiverName(shippingOrder.getReceiverName());
        response.setReceiverPhone(shippingOrder.getReceiverPhone());
        response.setReceiverEmail(null);
        response.setReceiverCountry(shippingOrder.getReceiverCountry());
        response.setReceiverState(shippingOrder.getReceiverProvince());
        response.setReceiverCity(shippingOrder.getReceiverCity());
        response.setReceiverAddress(shippingOrder.getReceiverAddress());
        response.setReceiverZipCode(shippingOrder.getReceiverZipCode());
        response.setActualWeight(shippingOrder.getWeight());
        response.setVolumeWeight(shippingOrder.getVolumeWeight());
        response.setChargeableWeight(shippingOrder.getChargeableWeight());
        response.setShippingFee(shippingOrder.getShippingFee());
        response.setCurrency(shippingOrder.getCurrency());
        response.setInsuranceFee(shippingOrder.getInsuranceFee());
        response.setCustomsFee(shippingOrder.getCustomsFee());
        response.setTotalFee(shippingOrder.getTotalFee());

        // 时间字段转换
        if (shippingOrder.getEstimatedDeliveryDays() != null && shippingOrder.getCreateTime() != null) {
            response.setEstimatedDeliveryTime(
                shippingOrder.getCreateTime().plusDays(shippingOrder.getEstimatedDeliveryDays())
            );
        }
        response.setActualDeliveryTime(shippingOrder.getDeliveredTime());
        response.setPickupTime(shippingOrder.getShippedTime());

        response.setExceptionReason(null);
        response.setRemark(shippingOrder.getRemark());
        response.setCreateTime(shippingOrder.getCreateTime());
        response.setUpdateTime(shippingOrder.getUpdateTime());

        // 获取追踪信息
        List<TrackingInfoResponse> trackingInfos = trackingService.getTrackingInfo(shippingOrder.getTrackingNo());
        response.setTrackingInfos(trackingInfos);
        
        return response;
    }
}
