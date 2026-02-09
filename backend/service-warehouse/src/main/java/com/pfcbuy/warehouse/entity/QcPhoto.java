package com.pfcbuy.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 质检照片实体
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_qc_photo")
public class QcPhoto extends BaseEntity {
    
    /**
     * 包裹号
     */
    private String packageNo;
    
    /**
     * 照片类型（OVERVIEW/DETAIL/DEFECT/LABEL）
     */
    private String photoType;
    
    /**
     * 照片URL
     */
    private String photoUrl;
    
    /**
     * 照片缩略图URL
     */
    private String thumbnailUrl;
    
    /**
     * 照片顺序
     */
    private Integer sortOrder;
    
    /**
     * 照片描述
     */
    private String description;
    
    /**
     * 上传人ID
     */
    private Long uploadUserId;
}
