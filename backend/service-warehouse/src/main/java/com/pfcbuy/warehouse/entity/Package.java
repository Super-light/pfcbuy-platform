package com.pfcbuy.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 包裹实体
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_package")
public class Package extends BaseEntity {
    
    /**
     * 包裹号（系统生成）
     */
    private String packageNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单号（关联订单）
     */
    private String orderNo;
    
    /**
     * 包裹状态（PENDING_ARRIVAL/ARRIVED/QC_IN_PROGRESS/QC_PASSED/QC_FAILED/MERGED/SHIPPED/EXCEPTION）
     */
    private String status;
    
    /**
     * 快递公司
     */
    private String expressCompany;
    
    /**
     * 物流单号
     */
    private String trackingNo;
    
    /**
     * 发件人姓名
     */
    private String senderName;
    
    /**
     * 发件人电话
     */
    private String senderPhone;
    
    /**
     * 商品描述
     */
    private String productDescription;
    
    /**
     * 商品数量
     */
    private Integer quantity;
    
    /**
     * 申报价值
     */
    private BigDecimal declaredValue;
    
    /**
     * 币种
     */
    private String currency;
    
    /**
     * 实际重量（kg）
     */
    private BigDecimal actualWeight;
    
    /**
     * 体积重量（kg）
     */
    private BigDecimal volumeWeight;
    
    /**
     * 长度（cm）
     */
    private BigDecimal length;
    
    /**
     * 宽度（cm）
     */
    private BigDecimal width;
    
    /**
     * 高度（cm）
     */
    private BigDecimal height;
    
    /**
     * 入库时间
     */
    private LocalDateTime inboundTime;
    
    /**
     * 出库时间
     */
    private LocalDateTime outboundTime;
    
    /**
     * 存储天数
     */
    private Integer storageDays;
    
    /**
     * 存储费用
     */
    private BigDecimal storageFee;
    
    /**
     * 是否需要质检
     */
    private Boolean needQc;
    
    /**
     * 质检状态（PENDING/PASSED/FAILED）
     */
    private String qcStatus;
    
    /**
     * 质检备注
     */
    private String qcRemark;
    
    /**
     * 质检时间
     */
    private LocalDateTime qcTime;
    
    /**
     * 质检人员ID
     */
    private Long qcUserId;
    
    /**
     * 父包裹号（合并后的包裹）
     */
    private String parentPackageNo;
    
    /**
     * 是否已合并
     */
    private Boolean merged;
    
    /**
     * 异常原因
     */
    private String exceptionReason;
    
    /**
     * 备注
     */
    private String remark;
}
