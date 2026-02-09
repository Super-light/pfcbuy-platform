package com.pfcbuy.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单响应
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
public class OrderResponse {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单状态描述
     */
    private String orderStatusDesc;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付状态描述
     */
    private String payStatusDesc;

    /**
     * 商品总金额
     */
    private BigDecimal productAmount;

    /**
     * 服务费
     */
    private BigDecimal serviceFee;

    /**
     * 国际运费
     */
    private BigDecimal shippingFee;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 原始币种
     */
    private String originalCurrency;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货地址
     */
    private String receiverAddress;

    /**
     * 国家
     */
    private String country;

    /**
     * 物流方式
     */
    private String shippingMethod;

    /**
     * 物流单号
     */
    private String trackingNumber;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shipTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    /**
     * 取消时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 管理员备注
     */
    private String adminRemark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 订单项列表
     */
    private List<OrderItemResponse> items;

    /**
     * 订单项响应
     */
    @Data
    public static class OrderItemResponse {
        private Long id;
        private Long orderId;
        private Long productSnapshotId;
        private String platform;
        private String platformProductId;
        private String platformSkuId;
        private String title;
        private String skuAttributes;
        private BigDecimal price;
        private String currency;
        private Integer quantity;
        private BigDecimal subtotal;
        private String imageUrl;
        private String productUrl;
        private String status;
        private String remark;
    }
}
