package com.pfcbuy.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 运费试算响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingFeeResponse {
    
    /**
     * 可用线路列表
     */
    private List<RouteOption> routes;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteOption {
        /**
         * 物流渠道
         */
        private String channel;
        
        /**
         * 线路名称
         */
        private String routeName;
        
        /**
         * 计费重量（kg）
         */
        private BigDecimal chargeableWeight;
        
        /**
         * 运费
         */
        private BigDecimal shippingFee;
        
        /**
         * 保险费
         */
        private BigDecimal insuranceFee;
        
        /**
         * 总费用
         */
        private BigDecimal totalFee;
        
        /**
         * 币种
         */
        private String currency;
        
        /**
         * 预计送达天数
         */
        private Integer estimatedDays;
        
        /**
         * 是否支持追踪
         */
        private Boolean trackable;
        
        /**
         * 备注
         */
        private String remark;
    }
}
