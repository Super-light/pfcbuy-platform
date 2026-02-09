package com.pfcbuy.warehouse.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.warehouse.dto.*;
import com.pfcbuy.warehouse.service.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包裹管理控制器
 *
 * @author PfcBuy Team
 */
@Slf4j
@RestController
@RequestMapping("/api/warehouse/package")
@RequiredArgsConstructor
public class PackageController {
    
    private final PackageService packageService;
    
    /**
     * 包裹入库
     */
    @PostMapping("/inbound")
    public Result<PackageResponse> inbound(@Valid @RequestBody PackageInboundRequest request) {
        log.info("包裹入库请求: {}", request);
        PackageResponse response = packageService.inbound(request);
        return Result.success(response);
    }
    
    /**
     * 包裹质检
     */
    @PostMapping("/quality-check")
    public Result<PackageResponse> qualityCheck(@Valid @RequestBody QualityCheckRequest request) {
        log.info("包裹质检请求: {}", request);
        PackageResponse response = packageService.qualityCheck(request);
        return Result.success(response);
    }
    
    /**
     * 包裹合并
     */
    @PostMapping("/merge")
    public Result<PackageResponse> mergePackages(@Valid @RequestBody PackageMergeRequest request) {
        log.info("包裹合并请求: {}", request);
        PackageResponse response = packageService.mergePackages(request);
        return Result.success(response);
    }
    
    /**
     * 包裹出库
     */
    @PostMapping("/outbound/{packageNo}")
    public Result<PackageResponse> outbound(@PathVariable String packageNo) {
        log.info("包裹出库请求: {}", packageNo);
        PackageResponse response = packageService.outbound(packageNo);
        return Result.success(response);
    }
    
    /**
     * 获取包裹详情
     */
    @GetMapping("/{packageNo}")
    public Result<PackageResponse> getPackage(@PathVariable String packageNo) {
        PackageResponse response = packageService.getPackageByNo(packageNo);
        return Result.success(response);
    }
    
    /**
     * 获取用户包裹列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<PackageResponse>> getUserPackages(
            @PathVariable Long userId,
            @RequestParam(required = false) String status) {
        List<PackageResponse> response = packageService.getUserPackages(userId, status);
        return Result.success(response);
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Warehouse Service is running");
    }
}
