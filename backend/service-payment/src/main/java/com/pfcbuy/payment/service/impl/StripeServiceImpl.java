package com.pfcbuy.payment.service.impl;

import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.payment.config.StripeConfig;
import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Stripe 服务实现类
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final StripeConfig stripeConfig;

    @Override
    public PaymentIntent createPaymentIntent(CreatePaymentRequest request, Long userId) {
        try {
            // 验证货币是否支持
            if (!stripeConfig.getSupportedCurrenciesList().contains(request.getCurrency().toUpperCase())) {
                throw new BusinessException("不支持的货币类型: " + request.getCurrency());
            }

            // 将金额转换为最小货币单位（Stripe 使用分/cents）
            Long amountInCents = convertToSmallestUnit(request.getAmount(), request.getCurrency());

            // 构建 metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("orderNo", request.getOrderNo());
            metadata.put("userId", String.valueOf(userId));
            if (request.getMetadata() != null) {
                metadata.putAll(request.getMetadata());
            }

            // 创建 PaymentIntent 参数
            PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.getCurrency().toLowerCase())
                    .putAllMetadata(metadata)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    );

            // 如果指定了支付方式
            if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
                paramsBuilder.addPaymentMethodType(request.getPaymentMethod());
            }

            PaymentIntentCreateParams params = paramsBuilder.build();

            // 调用 Stripe API 创建 PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            log.info("成功创建 PaymentIntent: {}, orderNo: {}", paymentIntent.getId(), request.getOrderNo());
            return paymentIntent;

        } catch (StripeException e) {
            log.error("创建 PaymentIntent 失败: {}", e.getMessage(), e);
            throw new BusinessException("创建支付失败: " + e.getUserMessage());
        }
    }

    @Override
    public PaymentIntent getPaymentIntent(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            log.info("获取 PaymentIntent: {}, status: {}", paymentIntentId, paymentIntent.getStatus());
            return paymentIntent;
        } catch (StripeException e) {
            log.error("获取 PaymentIntent 失败: {}", e.getMessage(), e);
            throw new BusinessException("获取支付信息失败: " + e.getUserMessage());
        }
    }

    @Override
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            PaymentIntentCancelParams params = PaymentIntentCancelParams.builder().build();
            PaymentIntent canceledIntent = paymentIntent.cancel(params);
            
            log.info("成功取消 PaymentIntent: {}", paymentIntentId);
            return canceledIntent;
        } catch (StripeException e) {
            log.error("取消 PaymentIntent 失败: {}", e.getMessage(), e);
            throw new BusinessException("取消支付失败: " + e.getUserMessage());
        }
    }

    @Override
    public Refund createRefund(String chargeId, Long refundAmount) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setCharge(chargeId)
                    .setAmount(refundAmount)
                    .build();

            Refund refund = Refund.create(params);
            log.info("成功创建退款: {}, chargeId: {}, amount: {}", refund.getId(), chargeId, refundAmount);
            return refund;
        } catch (StripeException e) {
            log.error("创建退款失败: {}", e.getMessage(), e);
            throw new BusinessException("创建退款失败: " + e.getUserMessage());
        }
    }

    @Override
    public Event constructWebhookEvent(String payload, String signature) {
        try {
            Event event = Webhook.constructEvent(payload, signature, stripeConfig.getWebhookSecret());
            log.info("成功验证 Webhook 事件: {}", event.getType());
            return event;
        } catch (SignatureVerificationException e) {
            log.error("Webhook 签名验证失败: {}", e.getMessage());
            throw new BusinessException("Webhook 签名验证失败");
        }
    }

    /**
     * 将金额转换为最小货币单位
     * 大多数货币使用 cents (1/100)
     * 日元等使用最小单位本身
     */
    private Long convertToSmallestUnit(BigDecimal amount, String currency) {
        currency = currency.toUpperCase();
        
        // 零小数货币（如日元、韩元）
        if ("JPY".equals(currency) || "KRW".equals(currency)) {
            return amount.longValue();
        }
        
        // 其他货币乘以 100
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
}
