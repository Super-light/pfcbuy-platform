package com.pfcbuy.product.service;

import com.pfcbuy.product.dto.ProductResolveRequest;
import com.pfcbuy.product.dto.ProductResolveResponse;

import java.util.List;

/**
 * 商品服务接口
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public interface ProductService {

    /**
     * 解析商品信息
     * 
     * @param request 解析请求
     * @return 商品信息
     */
    ProductResolveResponse resolveProduct(ProductResolveRequest request);

    /**
     * 根据快照ID获取商品信息
     * 
     * @param snapshotId 快照ID
     * @return 商品信息
     */
    ProductResolveResponse getProductBySnapshotId(Long snapshotId);

    /**
     * 获取支持的平台列表
     * 
     * @return 平台列表
     */
    List<String> getSupportedPlatforms();
}
