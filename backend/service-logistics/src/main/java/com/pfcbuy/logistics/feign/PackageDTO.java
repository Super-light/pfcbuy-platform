package com.pfcbuy.logistics.feign;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 包裹DTO
 *
 * @author PfcBuy Team
 */
@Data
public class PackageDTO {
    private Long id;
    private String packageNo;
    private Long userId;
    private String status;
    private BigDecimal actualWeight;
    private BigDecimal volumeWeight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private LocalDateTime createTime;
}
