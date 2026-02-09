package com.pfcbuy.payment.channel;

import com.pfcbuy.common.enums.PaymentChannel;
import com.pfcbuy.payment.dto.CreatePaymentRequest;
import com.pfcbuy.payment.dto.PaymentResponse;

import java.math.BigDecimal;

/**
 * 支付渠道抽象接口
 * 所有支付渠道都需要实现这个接口
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
public interface PaymentChannelService {

    /**
     * 获取支付渠道类型
     */
    PaymentChannel getChannel();

    /**
     * 创建支付
     *
     * @param request 支付请求
     * @return 支付响应（包含支付链接或客户端密钥）
     */
    PaymentResponse createPayment(CreatePaymentRequest request);

    /**
     * 查询支付状态
     *
     * @param transactionId 第三方交易ID
     * @return 支付响应
     */
    PaymentResponse queryPayment(String transactionId);

    /**
     * 取消支付
     *
     * @param transactionId 第三方交易ID
     * @return 是否成功
     */
    boolean cancelPayment(String transactionId);

    /**
     * 退款
     *
     * @param transactionId 第三方交易ID
     * @param refundAmount  退款金额
     * @param reason        退款原因
     * @return 是否成功
     */
    boolean refund(String transactionId, BigDecimal refundAmount, String reason);

    /**
     * 验证回调签名
     *
     * @param payload   回调数据
     * @param signature 签名
     * @return 是否有效
     */
    boolean verifySignature(String payload, String signature);

    /**
     * 处理支付成功回调
     *
     * @param payload 回调数据
     */
    void handlePaymentSuccess(String payload);

    /**
     * 处理支付失败回调
     *
     * @param payload 回调数据
     */
    void handlePaymentFailure(String payload);
}
