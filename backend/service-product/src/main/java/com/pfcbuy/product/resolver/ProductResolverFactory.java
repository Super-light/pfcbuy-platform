package com.pfcbuy.product.resolver;

import com.pfcbuy.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品解析器工厂
 * 根据平台类型选择对应的解析器
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Slf4j
@Component
public class ProductResolverFactory {

    private final Map<String, ProductResolver> resolverMap = new ConcurrentHashMap<>();

    /**
     * 构造函数，自动注入所有解析器实现
     * 
     * @param resolvers Spring容器中的所有解析器实现
     */
    @Autowired
    public ProductResolverFactory(List<ProductResolver> resolvers) {
        for (ProductResolver resolver : resolvers) {
            String platform = resolver.getSupportedPlatform();
            resolverMap.put(platform.toUpperCase(), resolver);
            log.info("注册商品解析器: {}", platform);
        }
        log.info("商品解析器工厂初始化完成，共注册 {} 个解析器", resolverMap.size());
    }

    /**
     * 根据平台类型获取解析器
     * 
     * @param platform 平台类型
     * @return 对应的解析器
     * @throws BusinessException 如果平台不支持
     */
    public ProductResolver getResolver(String platform) {
        if (platform == null || platform.trim().isEmpty()) {
            throw new BusinessException("平台类型不能为空");
        }

        ProductResolver resolver = resolverMap.get(platform.toUpperCase());
        if (resolver == null) {
            throw new BusinessException("不支持的平台类型: " + platform + 
                "，当前支持的平台: " + resolverMap.keySet());
        }

        return resolver;
    }

    /**
     * 获取所有支持的平台列表
     * 
     * @return 平台列表
     */
    public List<String> getSupportedPlatforms() {
        return List.copyOf(resolverMap.keySet());
    }

    /**
     * 检查是否支持指定平台
     * 
     * @param platform 平台类型
     * @return 是否支持
     */
    public boolean isSupported(String platform) {
        return platform != null && resolverMap.containsKey(platform.toUpperCase());
    }
}
