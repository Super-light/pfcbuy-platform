package com.pfcbuy.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品搜索响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponse {
    
    /**
     * 商品快照ID
     */
    private Long id;
    
    /**
     * 平台类型
     */
    private String platform;
    
    /**
     * 平台商品ID
     */
    private String platformProductId;
    
    /**
     * 商品标题（高亮）
     */
    private String title;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 货币类型
     */
    private String currency;
    
    /**
     * 商品主图
     */
    private String mainImage;
    
    /**
     * 类目
     */
    private String category;
    
    /**
     * 品牌
     */
    private String brand;
    
    /**
     * 销量
     */
    private Long salesCount;
    
    /**
     * 评价数
     */
    private Long reviewCount;
    
    /**
     * 评分
     */
    private Double rating;
    
    /**
     * 商品URL
     */
    private String productUrl;
    
    /**
     * 快照时间
     */
    private LocalDateTime snapshotTime;
    
    /**
     * 搜索得分
     */
    private Float score;
    
    /**
     * 分页信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private Long total;
        private Integer pageNum;
        private Integer pageSize;
        private Integer totalPages;
        private List<ProductSearchResponse> items;
    }
}
