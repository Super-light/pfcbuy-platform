package com.pfcbuy.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.gateway.entity.ApiAccessLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * API访问日志Mapper
 */
@Mapper
public interface ApiAccessLogMapper extends BaseMapper<ApiAccessLog> {
}
