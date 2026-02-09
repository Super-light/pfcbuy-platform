package com.pfcbuy.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品解析响应DTO
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
public class ProductResolveResponse {

    /**
     * 快照ID（数据库主键）
     */
    private Long snapshotId;

    /**
     * 平台类型
     */
    private String platform;

    /**
     * 平台商品ID
     */
    private String platformProductId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 当前价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 库存状态
     */
    private Integer stock;

    /**
     * 主图URL
     */
    private String mainImage;

    /**
     * 图片列表
     */
    private List<String> images;

    /**
     * 商品URL
     */
    private String productUrl;

    /**
     * 可用的SKU列表
     */
    private List<SkuInfo> skus;

    /**
     * 是否有货
     */
    private Boolean available;

    /**
     * 快照时间
     */
    private Long snapshotTime;

    /**
     * SKU信息内部类
     */
    @Data
    public static class SkuInfo {
        /**
         * SKU ID
         */
        private String skuId;

        /**
         * SKU属性（如：颜色、尺寸等）
         */
        private Map<String, String> attributes;

        /**
         * SKU价格
         */
        private BigDecimal price;

        /**
         * SKU库存
         */
        private Integer stock;

        /**
         * SKU图片
         */
        private String image;
    }
}
