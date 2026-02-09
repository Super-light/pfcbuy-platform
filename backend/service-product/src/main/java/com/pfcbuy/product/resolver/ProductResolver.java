package com.pfcbuy.product.resolver;

import com.pfcbuy.product.dto.ProductResolveResponse;

/**
 * 商品解析器接口
 * 使用策略模式，为不同电商平台提供不同的解析实现
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
public interface ProductResolver {

    /**
     * 获取支持的平台类型
     * 
     * @return 平台类型代码（如：TAOBAO, JD, TMALL）
     */
    String getSupportedPlatform();

    /**
     * 解析商品信息
     * 
     * @param productUrl 商品URL或ID
     * @param skuId SKU ID（可选）
     * @return 商品解析结果
     */
    ProductResolveResponse resolve(String productUrl, String skuId);

    /**
     * 从URL中提取商品ID
     * 
     * @param productUrl 商品URL
     * @return 商品ID
     */
    String extractProductId(String productUrl);

    /**
     * 验证URL格式是否正确
     * 
     * @param productUrl 商品URL
     * @return 是否有效
     */
    boolean validateUrl(String productUrl);
}
