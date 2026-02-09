package com.pfcbuy.search.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 热门关键词实体
 *
 * @author PfcBuy Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_hot_keyword")
public class HotKeyword extends BaseEntity {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 关键词
     */
    private String keyword;
    
    /**
     * 搜索次数
     */
    private Long searchCount;
    
    /**
     * 排序权重
     */
    private Integer weight;
    
    /**
     * 是否显示
     */
    private Boolean visible;
}