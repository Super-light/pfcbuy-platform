package com.pfcbuy.payment.controller;

import com.pfcbuy.payment.service.PaymentService;
import com.pfcbuy.payment.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Stripe Webhook 控制器
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final StripeService stripeService;
    private final PaymentService paymentService;

    /**
     * 处理 Stripe Webhook 事件
     */
    @PostMapping("/stripe")
    public String handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature
    ) {
        log.info("收到 Stripe Webhook 请求");

        try {
            // 验证 Webhook 签名
            Event event = stripeService.constructWebhookEvent(payload, signature);
            
            // 处理不同类型的事件
            handleEvent(event);

            return "success";
        } catch (Exception e) {
            log.error("处理 Webhook 失败: {}", e.getMessage(), e);
            return "error";
        }
    }

    /**
     * 处理事件
     */
    private void handleEvent(Event event) {
        log.info("处理 Webhook 事件: type={}, id={}", event.getType(), event.getId());

        StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
        
        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded(stripeObject);
                break;
            case "payment_intent.payment_failed":
                handlePaymentIntentFailed(stripeObject);
                break;
            case "payment_intent.canceled":
                handlePaymentIntentCanceled(stripeObject);
                break;
            case "charge.refunded":
                handleChargeRefunded(stripeObject);
                break;
            default:
                log.info("未处理的事件类型: {}", event.getType());
        }
    }

    /**
     * 处理支付成功
     */
    private void handlePaymentIntentSucceeded(StripeObject stripeObject) {
        if (stripeObject instanceof PaymentIntent) {
            PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
            log.info("支付成功: paymentIntentId={}", paymentIntent.getId());
            
            // 在较新版本的 Stripe SDK 中，Charge ID 可以从 latest_charge 获取
            String chargeId = paymentIntent.getLatestCharge();
            
            paymentService.handlePaymentSucceeded(paymentIntent.getId(), chargeId);
        }
    }

    /**
     * 处理支付失败
     */
    private void handlePaymentIntentFailed(StripeObject stripeObject) {
        if (stripeObject instanceof PaymentIntent) {
            PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
            log.info("支付失败: paymentIntentId={}", paymentIntent.getId());
            
            String failureReason = paymentIntent.getLastPaymentError() != null 
                    ? paymentIntent.getLastPaymentError().getMessage() 
                    : "Unknown error";
            
            paymentService.handlePaymentFailed(paymentIntent.getId(), failureReason);
        }
    }

    /**
     * 处理支付取消
     */
    private void handlePaymentIntentCanceled(StripeObject stripeObject) {
        if (stripeObject instanceof PaymentIntent) {
            PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
            log.info("支付取消: paymentIntentId={}", paymentIntent.getId());
            // 可以在这里添加额外的处理逻辑
        }
    }

    /**
     * 处理退款
     */
    private void handleChargeRefunded(StripeObject stripeObject) {
        log.info("收到退款事件");
        // 退款逻辑已在 createRefund 方法中处理，这里可以做额外的通知
    }
}