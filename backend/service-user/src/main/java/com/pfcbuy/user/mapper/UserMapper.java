package com.pfcbuy.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名或邮箱查询用户
     *
     * @param usernameOrEmail 用户名或邮箱
     * @return 用户信息
     */
    User findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}
