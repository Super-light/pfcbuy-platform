package com.pfcbuy.payment.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.dto.PaymentResponse;
import com.pfcbuy.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付控制器
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 创建支付
     */
    @PostMapping
    public Result<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId
    ) {
        log.info("创建支付请求: orderNo={}, userId={}", request.getOrderNo(), userId);
        PaymentResponse response = paymentService.createPayment(request, userId);
        return Result.success(response);
    }

    /**
     * 根据订单号获取支付信息
     */
    @GetMapping("/order/{orderNo}")
    public Result<PaymentResponse> getPaymentByOrderNo(@PathVariable String orderNo) {
        log.info("查询支付信息: orderNo={}", orderNo);
        PaymentResponse response = paymentService.getPaymentByOrderNo(orderNo);
        return Result.success(response);
    }

    /**
     * 根据用户ID获取支付列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<PaymentResponse>> getPaymentsByUserId(@PathVariable Long userId) {
        log.info("查询用户支付列表: userId={}", userId);
        List<PaymentResponse> responses = paymentService.getPaymentsByUserId(userId);
        return Result.success(responses);
    }

    /**
     * 取消支付
     */
    @PostMapping("/{paymentId}/cancel")
    public Result<Void> cancelPayment(@PathVariable Long paymentId) {
        log.info("取消支付: paymentId={}", paymentId);
        paymentService.cancelPayment(paymentId);
        return Result.success(null);
    }

    /**
     * 创建退款
     */
    @PostMapping("/{paymentId}/refund")
    public Result<Void> createRefund(
            @PathVariable Long paymentId,
            @RequestParam BigDecimal refundAmount
    ) {
        log.info("创建退款: paymentId={}, refundAmount={}", paymentId, refundAmount);
        paymentService.createRefund(paymentId, refundAmount);
        return Result.success(null);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Payment Service is running");
    }
}
