package com.pfcbuy.product.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.product.dto.ProductResolveRequest;
import com.pfcbuy.product.dto.ProductResolveResponse;
import com.pfcbuy.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 * 提供商品解析相关的REST API
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 解析商品信息
     * 
     * POST /api/v1/products/resolve
     * 
     * @param request 解析请求
     * @return 商品信息
     */
    @PostMapping("/resolve")
    public Result<ProductResolveResponse> resolveProduct(
            @Validated @RequestBody ProductResolveRequest request) {
        
        log.info("收到商品解析请求: platform={}, url={}", 
                request.getPlatform(), request.getProductUrl());
        
        ProductResolveResponse response = productService.resolveProduct(request);
        
        return Result.success(response);
    }

    /**
     * 根据快照ID获取商品信息
     * 
     * GET /api/v1/products/snapshot/{snapshotId}
     * 
     * @param snapshotId 快照ID
     * @return 商品信息
     */
    @GetMapping("/snapshot/{snapshotId}")
    public Result<ProductResolveResponse> getProductBySnapshot(
            @PathVariable Long snapshotId) {
        
        log.info("查询商品快照: {}", snapshotId);
        
        ProductResolveResponse response = productService.getProductBySnapshotId(snapshotId);
        
        return Result.success(response);
    }

    /**
     * 获取支持的平台列表
     * 
     * GET /api/v1/products/platforms
     * 
     * @return 平台列表
     */
    @GetMapping("/platforms")
    public Result<List<String>> getSupportedPlatforms() {
        log.info("查询支持的平台列表");
        
        List<String> platforms = productService.getSupportedPlatforms();
        
        return Result.success(platforms);
    }

    /**
     * 健康检查
     * 
     * GET /api/v1/products/health
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Product Service is running");
    }
}
