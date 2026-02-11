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
     * 物流订单ID
     */
    private Long shippingId;

    /**
     * 物流单号
     */
    private String shippingNo;

    /**
     * 物流跟踪号
     */
    private String trackingNo;

    /**
     * 状态
     */
    private String status;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 位置
     */
    private String location;
    
    /**
     * 发生时间
     */
    private LocalDateTime occurTime;

    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 备注
     */
    private String remark;
}
