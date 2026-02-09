package com.pfcbuy.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.logistics.entity.ShippingOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流订单Mapper
 *
 * @author PfcBuy Team
 */
@Mapper
public interface ShippingOrderMapper extends BaseMapper<ShippingOrder> {
}
