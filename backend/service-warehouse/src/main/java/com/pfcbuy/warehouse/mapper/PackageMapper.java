package com.pfcbuy.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.warehouse.entity.Package;
import org.apache.ibatis.annotations.Mapper;

/**
 * 包裹Mapper
 *
 * @author PfcBuy Team
 */
@Mapper
public interface PackageMapper extends BaseMapper<Package> {
}
