package com.pfcbuy.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转运地址实体
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_warehouse_address")
public class WarehouseAddress extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 地址编号（用户专属）
     */
    private String addressCode;
    
    /**
     * 收件人姓名
     */
    private String receiverName;
    
    /**
     * 收件人电话
     */
    private String receiverPhone;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 省/州
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区/县
     */
    private String district;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 邮编
     */
    private String postalCode;
    
    /**
     * 是否默认地址
     */
    private Boolean isDefault;
    
    /**
     * 地址类型（WAREHOUSE/PERSONAL）
     */
    private String addressType;
    
    /**
     * 备注
     */
    private String remark;
}
