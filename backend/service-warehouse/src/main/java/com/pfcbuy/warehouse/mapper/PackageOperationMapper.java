package com.pfcbuy.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.warehouse.entity.PackageOperation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 包裹操作记录Mapper
 *
 * @author PfcBuy Team
 */
@Mapper
public interface PackageOperationMapper extends BaseMapper<PackageOperation> {
}
