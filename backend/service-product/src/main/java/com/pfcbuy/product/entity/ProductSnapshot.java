package com.pfcbuy.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品快照实体
 * 用于保存从电商平台解析的商品信息的历史快照
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_product_snapshot", autoResultMap = true)
public class ProductSnapshot extends BaseEntity {

    /**
     * 平台类型：TAOBAO, JD, TMALL等
     */
    private String platform;

    /**
     * 平台商品ID
     */
    private String platformProductId;

    /**
     * 平台SKU ID
     */
    private String platformSkuId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * SKU属性（JSON）
     * 例如：{"color": "红色", "size": "M"}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> skuAttributes;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 原价（划线价）
     */
    private BigDecimal originalPrice;

    /**
     * 货币类型：CNY, USD等
     */
    private String currency;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 商品主图
     */
    private String mainImage;

    /**
     * 商品图片列表（JSON数组）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    /**
     * 商品URL
     */
    private String productUrl;

    /**
     * 商品详情HTML（可选，用于展示）
     */
    @TableField(exist = false)
    private String detailHtml;

    /**
     * 快照时间
     */
    private LocalDateTime snapshotTime;

    /**
     * 是否可用（商品是否下架）
     */
    private Boolean available;
}
