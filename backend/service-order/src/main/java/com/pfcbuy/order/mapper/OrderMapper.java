package com.pfcbuy.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单Mapper
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 更新订单状态
     *
     * @param id 订单ID
     * @param orderStatus 订单状态
     * @return 影响行数
     */
    int updateOrderStatus(@Param("id") Long id, @Param("orderStatus") String orderStatus);

    /**
     * 更新支付状态
     *
     * @param id 订单ID
     * @param payStatus 支付状态
     * @return 影响行数
     */
    int updatePayStatus(@Param("id") Long id, @Param("payStatus") String payStatus);
}
