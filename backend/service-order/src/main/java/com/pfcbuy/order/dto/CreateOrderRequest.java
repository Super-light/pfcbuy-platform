package com.pfcbuy.order.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * 创建订单请求
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Data
public class CreateOrderRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 订单项列表
     */
    @NotEmpty(message = "订单项不能为空")
    @Valid
    private List<OrderItemRequest> items;

    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    /**
     * 用户备注
     */
    @Size(max = 500, message = "备注长度不能超过500字符")
    private String userRemark;

    /**
     * 订单项请求
     */
    @Data
    public static class OrderItemRequest {

        /**
         * 商品快照ID
         */
        @NotNull(message = "商品快照ID不能为空")
        private Long productSnapshotId;

        /**
         * SKU ID
         */
        private String skuId;

        /**
         * 数量
         */
        @NotNull(message = "数量不能为空")
        @Min(value = 1, message = "数量至少为1")
        @Max(value = 999, message = "数量不能超过999")
        private Integer quantity;

        /**
         * 备注
         */
        @Size(max = 200, message = "备注长度不能超过200字符")
        private String remark;
    }
}
