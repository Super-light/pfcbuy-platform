package com.pfcbuy.payment.feign;

import com.pfcbuy.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单服务 Feign 客户端
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@FeignClient(
        name = "service-order",
        url = "${feign.order-service.url}",
        path = "/order"
)
public interface OrderFeignClient {

    /**
     * 更新订单支付状态
     *
     * @param orderNo   订单号
     * @param paymentId 支付ID
     * @return 更新结果
     */
    @PostMapping("/orders/{orderNo}/payment")
    Result<Void> updatePaymentStatus(
            @PathVariable("orderNo") String orderNo,
            @RequestParam("paymentId") String paymentId
    );

    /**
     * 获取订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    @PostMapping("/orders/{orderNo}")
    Result<OrderDTO> getOrder(@PathVariable("orderNo") String orderNo);
}
