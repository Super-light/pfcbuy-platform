package com.pfcbuy.logistics.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.logistics.dto.TrackingInfoResponse;
import com.pfcbuy.logistics.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物流追踪Controller
 *
 * @author PfcBuy Team
 */
@Slf4j
@RestController
@RequestMapping("/api/logistics/tracking")
@RequiredArgsConstructor
public class TrackingController {
    
    private final TrackingService trackingService;
    
    /**
     * 获取物流追踪信息
     */
    @GetMapping("/{trackingNo}")
    public Result<List<TrackingInfoResponse>> getTrackingInfo(@PathVariable String trackingNo) {
        List<TrackingInfoResponse> trackingInfos = trackingService.getTrackingInfo(trackingNo);
        return Result.success(trackingInfos);
    }
    
    /**
     * 同步物流追踪信息（从物流商API）
     */
    @PostMapping("/sync/{shippingOrderNo}")
    public Result<Void> syncTrackingInfo(@PathVariable String shippingOrderNo) {
        trackingService.syncTrackingInfo(shippingOrderNo);
        return Result.success(null);
    }
    
    /**
     * 手动添加追踪信息
     */
    @PostMapping("/add")
    public Result<Void> addTrackingInfo(@RequestParam String shippingOrderNo,
                                        @RequestParam String status,
                                        @RequestParam String description,
                                        @RequestParam(required = false) String location) {
        trackingService.addTrackingInfo(shippingOrderNo, status, description, location);
        return Result.success(null);
    }
}
