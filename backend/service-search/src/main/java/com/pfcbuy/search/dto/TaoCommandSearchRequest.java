package com.pfcbuy.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 淘口令搜索请求
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaoCommandSearchRequest {
    
    /**
     * 淘口令内容
     */
    @NotBlank(message = "淘口令不能为空")
    private String taoCommand;
    
    /**
     * 用户ID
     */
    private Long userId;
}
