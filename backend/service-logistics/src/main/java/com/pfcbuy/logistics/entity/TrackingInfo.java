package com.pfcbuy.logistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物流追踪信息实体
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_tracking_info")
public class TrackingInfo extends BaseEntity {
    
    /**
     * 物流订单号
     */
    private String shippingOrderNo;
    
    /**
     * 物流单号
     */
    private String trackingNo;
    
    /**
     * 物流渠道
     */
    private String channel;
    
    /**
     * 追踪状态
     */
    private String status;
    
    /**
     * 追踪描述
     */
    private String description;
    
    /**
     * 位置信息
     */
    private String location;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 事件时间
     */
    private LocalDateTime eventTime;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 备注
     */
    private String remark;
}
