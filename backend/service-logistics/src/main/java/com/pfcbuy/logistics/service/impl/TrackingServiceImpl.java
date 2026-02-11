package com.pfcbuy.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.logistics.dto.TrackingInfoResponse;
import com.pfcbuy.logistics.entity.ShippingOrder;
import com.pfcbuy.logistics.entity.TrackingInfo;
import com.pfcbuy.logistics.mapper.ShippingOrderMapper;
import com.pfcbuy.logistics.mapper.TrackingInfoMapper;
import com.pfcbuy.logistics.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物流追踪服务实现
 *
 * @author PfcBuy Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {
    
    private final TrackingInfoMapper trackingInfoMapper;
    private final ShippingOrderMapper shippingOrderMapper;
    
    @Override
    public List<TrackingInfoResponse> getTrackingInfo(String trackingNo) {
        LambdaQueryWrapper<TrackingInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrackingInfo::getTrackingNo, trackingNo)
                .orderByDesc(TrackingInfo::getOccurTime);

        List<TrackingInfo> trackingInfos = trackingInfoMapper.selectList(wrapper);
        
        return trackingInfos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTrackingInfo(String shippingOrderNo) {
        // 获取物流订单
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getShippingNo, shippingOrderNo);
        ShippingOrder shippingOrder = shippingOrderMapper.selectOne(wrapper);
        
        if (shippingOrder == null) {
            throw new BusinessException("物流订单不存在");
        }
        
        // TODO: 根据不同的物流渠道调用对应的API获取追踪信息
        // 这里模拟从物流商API获取数据
        log.info("同步物流追踪信息，渠道: {}, 单号: {}", 
                shippingOrder.getCarrier(), shippingOrder.getTrackingNo());

        // 实际项目中这里应该调用物流商API
        // 例如：DHLClient.getTrackingInfo(trackingNo)
        //      FedExClient.getTrackingInfo(trackingNo)
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTrackingInfo(String shippingOrderNo, String status, 
                                 String description, String location) {
        // 获取物流订单
        LambdaQueryWrapper<ShippingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingOrder::getShippingNo, shippingOrderNo);
        ShippingOrder shippingOrder = shippingOrderMapper.selectOne(wrapper);
        
        if (shippingOrder == null) {
            throw new BusinessException("物流订单不存在");
        }
        
        // 创建追踪信息
        TrackingInfo trackingInfo = TrackingInfo.builder()
                .shippingId(shippingOrder.getId())
                .shippingNo(shippingOrderNo)
                .trackingNo(shippingOrder.getTrackingNo())
                .status(status)
                .description(description)
                .location(location)
                .occurTime(LocalDateTime.now())
                .build();
        
        trackingInfoMapper.insert(trackingInfo);
        
        log.info("添加追踪信息成功，订单号: {}, 状态: {}", shippingOrderNo, status);
    }
    
    /**
     * 转换为响应对象
     */
    private TrackingInfoResponse convertToResponse(TrackingInfo trackingInfo) {
        TrackingInfoResponse response = new TrackingInfoResponse();
        BeanUtils.copyProperties(trackingInfo, response);
        return response;
    }
}
