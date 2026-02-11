package com.pfcbuy.product.service.impl;

import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.product.dto.ProductResolveRequest;
import com.pfcbuy.product.dto.ProductResolveResponse;
import com.pfcbuy.product.entity.ProductSnapshot;
import com.pfcbuy.product.mapper.ProductSnapshotMapper;
import com.pfcbuy.product.resolver.ProductResolver;
import com.pfcbuy.product.resolver.ProductResolverFactory;
import com.pfcbuy.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品服务实现类
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private static final String CACHE_KEY_PREFIX = "product:";
    private static final long CACHE_EXPIRE_MINUTES = 30;

    @Autowired
    private ProductResolverFactory resolverFactory;

    @Autowired
    private ProductSnapshotMapper snapshotMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResolveResponse resolveProduct(ProductResolveRequest request) {
        log.info("开始解析商品，平台: {}, URL: {}", request.getPlatform(), request.getProductUrl());

        // 1. 获取对应平台的解析器
        ProductResolver resolver = resolverFactory.getResolver(request.getPlatform());

        // 2. 提取商品ID
        String productId = resolver.extractProductId(request.getProductUrl());

        // 3. 检查缓存（如果不强制刷新）
        if (!Boolean.TRUE.equals(request.getForceRefresh())) {
            ProductResolveResponse cachedProduct = getFromCache(request.getPlatform(), productId);
            if (cachedProduct != null) {
                log.info("从缓存获取商品信息: {}", productId);
                return cachedProduct;
            }
        }

        // 4. 调用解析器解析商品
        ProductResolveResponse response;
        try {
            response = resolver.resolve(request.getProductUrl(), request.getSkuId());
        } catch (Exception e) {
            log.error("解析商品失败: {}", e.getMessage(), e);
            throw new BusinessException("解析商品失败: " + e.getMessage());
        }

        // 5. 保存快照到数据库
        ProductSnapshot snapshot = saveSnapshot(response);
        response.setSnapshotId(snapshot.getId());

        // 6. 缓存结果
        cacheProduct(request.getPlatform(), productId, response);

        log.info("商品解析完成，快照ID: {}", snapshot.getId());
        return response;
    }

    @Override
    public ProductResolveResponse getProductBySnapshotId(Long snapshotId) {
        log.info("根据快照ID获取商品: {}", snapshotId);

        ProductSnapshot snapshot = snapshotMapper.selectById(snapshotId);
        if (snapshot == null) {
            throw new BusinessException("商品快照不存在: " + snapshotId);
        }

        return convertToResponse(snapshot);
    }

    @Override
    public List<String> getSupportedPlatforms() {
        return resolverFactory.getSupportedPlatforms();
    }

    /**
     * 保存商品快照
     * 
     * @param response 商品信息
     * @return 快照实体
     */
    private ProductSnapshot saveSnapshot(ProductResolveResponse response) {
        ProductSnapshot snapshot = new ProductSnapshot();
        
        snapshot.setPlatform(response.getPlatform());
        snapshot.setPlatformProductId(response.getPlatformProductId());
        snapshot.setTitle(response.getTitle());
        snapshot.setPrice(response.getPrice());
        snapshot.setOriginalPrice(response.getOriginalPrice());
        snapshot.setCurrency(response.getCurrency());
        snapshot.setStock(response.getStock());
        snapshot.setMainImage(response.getMainImage());
        snapshot.setImages(response.getImages());
        snapshot.setProductUrl(response.getProductUrl());
        snapshot.setSnapshotTime(LocalDateTime.ofInstant(
            Instant.ofEpochMilli(response.getSnapshotTime()),
            ZoneId.systemDefault()
        ));
        snapshot.setAvailable(response.getAvailable());

        snapshotMapper.insert(snapshot);
        
        log.info("商品快照已保存，ID: {}", snapshot.getId());
        return snapshot;
    }

    /**
     * 将快照转换为响应DTO
     * 
     * @param snapshot 快照实体
     * @return 响应DTO
     */
    private ProductResolveResponse convertToResponse(ProductSnapshot snapshot) {
        ProductResolveResponse response = new ProductResolveResponse();
        BeanUtils.copyProperties(snapshot, response);
        response.setSnapshotId(snapshot.getId());

        // 转换时间类型：LocalDateTime -> Long
        if (snapshot.getSnapshotTime() != null) {
            response.setSnapshotTime(
                snapshot.getSnapshotTime()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            );
        }

        return response;
    }

    /**
     * 从缓存获取商品
     * 
     * @param platform 平台
     * @param productId 商品ID
     * @return 商品信息，如果不存在返回null
     */
    private ProductResolveResponse getFromCache(String platform, String productId) {
        try {
            String cacheKey = buildCacheKey(platform, productId);
            return (ProductResolveResponse) redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            log.warn("从缓存获取商品失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 缓存商品信息
     * 
     * @param platform 平台
     * @param productId 商品ID
     * @param response 商品信息
     */
    private void cacheProduct(String platform, String productId, ProductResolveResponse response) {
        try {
            String cacheKey = buildCacheKey(platform, productId);
            redisTemplate.opsForValue().set(cacheKey, response, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.debug("商品已缓存: {}", cacheKey);
        } catch (Exception e) {
            log.warn("缓存商品失败: {}", e.getMessage());
        }
    }

    /**
     * 构建缓存键
     * 
     * @param platform 平台
     * @param productId 商品ID
     * @return 缓存键
     */
    private String buildCacheKey(String platform, String productId) {
        return CACHE_KEY_PREFIX + platform + ":" + productId;
    }
}
