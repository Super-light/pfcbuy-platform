package com.pfcbuy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATED("CREATED", "已创建"),
    PAID("PAID", "已支付"),
    PURCHASING("PURCHASING", "采购中"),
    IN_WAREHOUSE("IN_WAREHOUSE", "已入库"),
    QC_COMPLETED("QC_COMPLETED", "质检完成"),
    SHIPPED("SHIPPED", "已发货"),
    DELIVERED("DELIVERED", "已签收"),
    CANCELLED("CANCELLED", "已取消");

    private final String code;
    private final String description;

    /**
     * 根据码获取描述
     */
    public static String getDescByCode(String code) {
        for (OrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDescription();
            }
        }
        return "未知状态";
    }

    /**
     * 根据码获取枚举
     */
    public static OrderStatus getByCode(String code) {
        for (OrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的订单状态: " + code);
    }
}
