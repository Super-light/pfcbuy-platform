package com.pfcbuy.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 质检照片响应
 *
 * @author PfcBuy Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QcPhotoResponse {
    
    private Long id;
    private String packageNo;
    private String photoType;
    private String photoUrl;
    private String thumbnailUrl;
    private Integer sortOrder;
    private String description;
    private Long uploadUserId;
    private LocalDateTime createTime;
}
