package com.pfcbuy.order.dto;

import lombok.Data;

/**
 * 订单查询请求
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
public class OrderQueryRequest {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
