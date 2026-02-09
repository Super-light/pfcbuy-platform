package com.pfcbuy.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单项Mapper
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单ID查询订单项列表
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量插入订单项
     *
     * @param items 订单项列表
     * @return 影响行数
     */
    int batchInsert(@Param("items") List<OrderItem> items);
}
