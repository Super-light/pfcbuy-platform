package com.pfcbuy.gateway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * API访问日志
 */
@Data
@TableName("t_api_access_log")
public class ApiAccessLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 请求ID（链路追踪）
     */
    private String requestId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应状态码
     */
    private Integer responseStatus;

    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * User-Agent
     */
    private String userAgent;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
