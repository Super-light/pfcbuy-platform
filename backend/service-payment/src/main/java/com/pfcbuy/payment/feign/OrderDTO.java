package com.pfcbuy.payment.feign;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单 DTO（用于 Feign 调用）
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
public class OrderDTO {

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
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 支付状态
     */
    private String payStatus;
}
