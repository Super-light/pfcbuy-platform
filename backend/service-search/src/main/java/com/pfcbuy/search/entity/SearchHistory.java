package com.pfcbuy.search.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索历史实体
 *
 * @author PfcBuy Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_search_history")
public class SearchHistory extends BaseEntity {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 搜索类型（KEYWORD/LINK/IMAGE/TAO_COMMAND）
     */
    private String searchType;
    
    /**
     * 搜索结果数量
     */
    private Integer resultCount;
    
    /**
     * 用户IP
     */
    private String userIp;
    
    /**
     * 用户UA
     */
    private String userAgent;
}