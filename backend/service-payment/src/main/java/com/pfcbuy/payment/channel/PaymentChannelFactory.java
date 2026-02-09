package com.pfcbuy.payment.channel;

import com.pfcbuy.common.enums.PaymentChannel;
import com.pfcbuy.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支付渠道工厂
 * 根据支付渠道类型获取对应的服务实现
 *
 * @author PfcBuy Team
 * @since 2024-02-04
 */
@Component
@RequiredArgsConstructor
public class PaymentChannelFactory {

    private final List<PaymentChannelService> paymentChannelServices;

    /**
     * 渠道服务缓存Map
     */
    private Map<PaymentChannel, PaymentChannelService> serviceMap;

    /**
     * 根据支付渠道获取对应的服务
     *
     * @param channel 支付渠道
     * @return 支付渠道服务
     */
    public PaymentChannelService getService(PaymentChannel channel) {
        if (serviceMap == null) {
            // 初始化缓存Map
            serviceMap = paymentChannelServices.stream()
                    .collect(Collectors.toMap(
                            PaymentChannelService::getChannel,
                            Function.identity()
                    ));
        }

        PaymentChannelService service = serviceMap.get(channel);
        if (service == null) {
            throw new BusinessException("不支持的支付渠道: " + channel);
        }

        return service;
    }

    /**
     * 根据支付渠道代码获取对应的服务
     *
     * @param channelCode 支付渠道代码
     * @return 支付渠道服务
     */
    public PaymentChannelService getService(String channelCode) {
        PaymentChannel channel = PaymentChannel.fromCode(channelCode);
        return getService(channel);
    }
}
