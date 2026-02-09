package com.pfcbuy.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pfcbuy.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user")
public class User extends BaseEntity {
    
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名（唯一）
     */
    private String username;
    
    /**
     * 密码（加密存储）
     */
    private String password;
    
    /**
     * 邮箱（唯一）
     */
    private String email;
    
    /**
     * 手机号（唯一）
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别 (0:未知 1:男 2:女)
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 语言偏好
     */
    private String language;
    
    /**
     * 货币偏好
     */
    private String currency;
    
    /**
     * 会员等级
     */
    private String memberLevel;
    
    /**
     * 账户余额
     */
    private BigDecimal balance;
    
    /**
     * 积分
     */
    private Integer points;
    
    /**
     * 用户状态
     */
    private String status;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    /**
     * 注册IP
     */
    private String registerIp;
    
    /**
     * 备注
     */
    private String remark;
}
