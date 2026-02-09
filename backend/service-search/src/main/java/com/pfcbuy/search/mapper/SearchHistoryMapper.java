package com.pfcbuy.search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.search.entity.SearchHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索历史Mapper
 *
 * @author PfcBuy Team
 */
@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {
}
