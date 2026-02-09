package com.pfcbuy.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品快照DTO（用于Feign调用）
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
public class ProductSnapshotDTO {

    private Long id;
    private String platform;
    private String platformProductId;
    private String platformSkuId;
    private String title;
    private String subtitle;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String currency;
    private Integer stock;
    private String mainImage;
    private List<String> images;
    private String productUrl;
    private Boolean available;
    private String skuAttributes;
}
