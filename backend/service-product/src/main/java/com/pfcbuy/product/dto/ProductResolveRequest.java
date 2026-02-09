package com.pfcbuy.product.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 商品解析请求DTO
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
public class ProductResolveRequest {

    /**
     * 平台类型：TAOBAO, JD, TMALL等
     */
    @NotBlank(message = "平台类型不能为空")
    private String platform;

    /**
     * 商品URL或商品ID
     */
    @NotBlank(message = "商品URL或ID不能为空")
    private String productUrl;

    /**
     * SKU ID（可选，如果不指定则返回默认SKU）
     */
    private String skuId;

    /**
     * 是否强制刷新（不使用缓存）
     */
    private Boolean forceRefresh = false;
}
