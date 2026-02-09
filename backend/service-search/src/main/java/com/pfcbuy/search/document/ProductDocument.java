package com.pfcbuy.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品搜索文档（Elasticsearch）
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "pfcbuy_products")
public class ProductDocument {
    
    /**
     * 商品快照ID
     */
    @Id
    private Long id;
    
    /**
     * 平台类型
     */
    @Field(type = FieldType.Keyword)
    private String platform;
    
    /**
     * 平台商品ID
     */
    @Field(type = FieldType.Keyword)
    private String platformProductId;
    
    /**
     * 商品标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;
    
    /**
     * 商品描述
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String description;
    
    /**
     * 商品价格
     */
    @Field(type = FieldType.Double)
    private BigDecimal price;
    
    /**
     * 货币类型
     */
    @Field(type = FieldType.Keyword)
    private String currency;
    
    /**
     * 商品主图
     */
    @Field(type = FieldType.Keyword)
    private String mainImage;
    
    /**
     * 商品图片列表
     */
    @Field(type = FieldType.Keyword)
    private List<String> images;
    
    /**
     * SKU列表（JSON字符串）
     */
    @Field(type = FieldType.Text, index = false)
    private String skus;
    
    /**
     * 类目
     */
    @Field(type = FieldType.Keyword)
    private String category;
    
    /**
     * 品牌
     */
    @Field(type = FieldType.Keyword)
    private String brand;
    
    /**
     * 销量
     */
    @Field(type = FieldType.Long)
    private Long salesCount;
    
    /**
     * 评价数
     */
    @Field(type = FieldType.Long)
    private Long reviewCount;
    
    /**
     * 评分
     */
    @Field(type = FieldType.Double)
    private Double rating;
    
    /**
     * 是否可用
     */
    @Field(type = FieldType.Boolean)
    private Boolean available;
    
    /**
     * 商品URL
     */
    @Field(type = FieldType.Keyword)
    private String productUrl;
    
    /**
     * 快照时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime snapshotTime;
    
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;
}
