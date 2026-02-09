package com.pfcbuy.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单项实体
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_item")
public class OrderItem extends BaseEntity {

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品快照ID（关联product_snapshot表）
     */
    @TableField("product_snapshot_id")
    private Long productSnapshotId;

    /**
     * 平台类型
     */
    @TableField("platform")
    private String platform;

    /**
     * 平台商品ID
     */
    @TableField("platform_product_id")
    private String platformProductId;

    /**
     * 平台SKU ID
     */
    @TableField("platform_sku_id")
    private String platformSkuId;

    /**
     * 商品标题（快照）
     */
    @TableField("title")
    private String title;

    /**
     * SKU属性（快照，JSON格式）
     */
    @TableField("sku_attributes")
    private String skuAttributes;

    /**
     * 单价（快照）
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 小计金额
     */
    @TableField("subtotal")
    private BigDecimal subtotal;

    /**
     * 商品图片URL
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 商品URL
     */
    @TableField("product_url")
    private String productUrl;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
