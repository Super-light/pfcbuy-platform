package com.pfcbuy.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
public class Order extends BaseEntity {

    /**
     * 订单号（唯一）
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 订单状态
     * CREATED - 已创建
     * PAID - 已支付
     * PURCHASING - 采购中
     * IN_WAREHOUSE - 已入库
     * SHIPPED - 已发货
     * COMPLETED - 已完成
     * CANCELLED - 已取消
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 支付状态
     * UNPAID - 未支付
     * PART_PAID - 部分支付
     * PAID - 已支付
     * REFUNDING - 退款中
     * REFUNDED - 已退款
     */
    @TableField("pay_status")
    private String payStatus;

    /**
     * 商品总金额
     */
    @TableField("product_amount")
    private BigDecimal productAmount;

    /**
     * 服务费
     */
    @TableField("service_fee")
    private BigDecimal serviceFee;

    /**
     * 国际运费
     */
    @TableField("shipping_fee")
    private BigDecimal shippingFee;

    /**
     * 订单总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 已支付金额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;

    /**
     * 币种（用户支付币种）
     */
    @TableField("currency")
    private String currency;

    /**
     * 原始币种（商品币种，通常为CNY）
     */
    @TableField("original_currency")
    private String originalCurrency;

    /**
     * 汇率
     */
    @TableField("exchange_rate")
    private BigDecimal exchangeRate;

    /**
     * 收货地址ID
     */
    @TableField("address_id")
    private Long addressId;

    /**
     * 收货人姓名
     */
    @TableField("receiver_name")
    private String receiverName;

    /**
     * 收货人电话
     */
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @TableField("receiver_address")
    private String receiverAddress;

    /**
     * 国家
     */
    @TableField("country")
    private String country;

    /**
     * 物流方式
     */
    @TableField("shipping_method")
    private String shippingMethod;

    /**
     * 物流单号
     */
    @TableField("tracking_number")
    private String trackingNumber;

    /**
     * 订单过期时间（未支付订单的过期时间）
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    @TableField("ship_time")
    private LocalDateTime shipTime;

    /**
     * 完成时间
     */
    @TableField("complete_time")
    private LocalDateTime completeTime;

    /**
     * 取消时间
     */
    @TableField("cancel_time")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @TableField("cancel_reason")
    private String cancelReason;

    /**
     * 用户备注
     */
    @TableField("user_remark")
    private String userRemark;

    /**
     * 管理员备注
     */
    @TableField("admin_remark")
    private String adminRemark;
}
