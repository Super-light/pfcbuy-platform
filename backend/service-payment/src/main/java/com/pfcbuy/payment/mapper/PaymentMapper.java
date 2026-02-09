package com.pfcbuy.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.payment.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 支付 Mapper 接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

    /**
     * 根据订单号查询支付记录
     */
    @Select("SELECT * FROM payment WHERE order_no = #{orderNo} AND is_deleted = 0")
    Payment selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据 Stripe PaymentIntent ID 查询
     */
    @Select("SELECT * FROM payment WHERE stripe_payment_intent_id = #{paymentIntentId} AND is_deleted = 0")
    Payment selectByPaymentIntentId(@Param("paymentIntentId") String paymentIntentId);

    /**
     * 根据用户ID查询支付列表
     */
    @Select("SELECT * FROM payment WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<Payment> selectByUserId(@Param("userId") Long userId);

    /**
     * 更新支付状态
     */
    @Update("UPDATE payment SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 更新支付为成功状态
     */
    @Update("UPDATE payment SET status = #{status}, stripe_charge_id = #{chargeId}, paid_at = NOW(), update_time = NOW() WHERE id = #{id}")
    int updateToSucceeded(@Param("id") Long id, @Param("status") String status, @Param("chargeId") String chargeId);
}
