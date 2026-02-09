package com.pfcbuy.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片搜索请求
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageSearchRequest {
    
    /**
     * 图片URL
     */
    @NotBlank(message = "图片URL不能为空")
    private String imageUrl;
    
    /**
     * 或者：Base64编码的图片
     */
    private String imageBase64;
    
    /**
     * 相似度阈值（0-1）
     */
    private Double similarityThreshold = 0.75;
    
    /**
     * 平台过滤
     */
    private String platform;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页数量
     */
    private Integer pageSize = 20;
    
    /**
     * 用户ID
     */
    private Long userId;
}
