package com.pfcbuy.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品搜索请求
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {
    
    /**
     * 搜索关键词
     */
    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;
    
    /**
     * 搜索类型：KEYWORD/LINK/IMAGE/TAO_COMMAND
     */
    private String searchType;
    
    /**
     * 平台过滤
     */
    private String platform;
    
    /**
     * 类目过滤
     */
    private String category;
    
    /**
     * 品牌过滤
     */
    private String brand;
    
    /**
     * 最低价格
     */
    private BigDecimal minPrice;
    
    /**
     * 最高价格
     */
    private BigDecimal maxPrice;
    
    /**
     * 排序字段：price/sales/rating/createTime
     */
    private String sortField;
    
    /**
     * 排序方式：asc/desc
     */
    private String sortOrder;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页数量
     */
    private Integer pageSize = 20;
    
    /**
     * 用户ID（用于记录搜索历史）
     */
    private Long userId;
}
