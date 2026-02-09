package com.pfcbuy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.search.document.ProductDocument;
import com.pfcbuy.search.dto.*;
import com.pfcbuy.search.entity.HotKeyword;
import com.pfcbuy.search.entity.SearchHistory;
import com.pfcbuy.search.feign.ProductFeignClient;
import com.pfcbuy.search.mapper.HotKeywordMapper;
import com.pfcbuy.search.mapper.SearchHistoryMapper;
import com.pfcbuy.search.repository.ProductSearchRepository;
import com.pfcbuy.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 搜索服务实现类
 *
 * @author PfcBuy Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    
    private final SearchHistoryMapper searchHistoryMapper;
    private final HotKeywordMapper hotKeywordMapper;
    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductFeignClient productFeignClient;
    
    @Override
    public ProductSearchResponse.PageInfo searchByKeyword(ProductSearchRequest request) {
        log.info("关键词搜索: keyword={}, platform={}", request.getKeyword(), request.getPlatform());
        
        // 构建Elasticsearch查询DSL
        StringBuilder queryString = new StringBuilder();
        queryString.append("{\n");
        queryString.append("  \"bool\": {\n");
        queryString.append("    \"should\": [\n");
        queryString.append("      { \"match\": { \"title\": { \"query\": \"").append(request.getKeyword()).append("\", \"boost\": 2.0 } } },\n");
        queryString.append("      { \"match\": { \"description\": { \"query\": \"").append(request.getKeyword()).append("\" } } }\n");
        queryString.append("    ],\n");
        queryString.append("    \"minimum_should_match\": 1,\n");
        queryString.append("    \"filter\": [\n");
        queryString.append("      { \"term\": { \"available\": true } }");
        
        // 平台过滤
        if (request.getPlatform() != null) {
            queryString.append(",\n      { \"term\": { \"platform\": \"").append(request.getPlatform()).append("\" } }");
        }
        
        // 类目过滤
        if (request.getCategory() != null) {
            queryString.append(",\n      { \"term\": { \"category\": \"").append(request.getCategory()).append("\" } }");
        }
        
        // 品牌过滤
        if (request.getBrand() != null) {
            queryString.append(",\n      { \"term\": { \"brand\": \"").append(request.getBrand()).append("\" } }");
        }
        
        // 价格过滤
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            queryString.append(",\n      { \"range\": { \"price\": { ");
            List<String> rangeConditions = new ArrayList<>();
            if (request.getMinPrice() != null) {
                rangeConditions.add("\"gte\": " + request.getMinPrice());
            }
            if (request.getMaxPrice() != null) {
                rangeConditions.add("\"lte\": " + request.getMaxPrice());
            }
            queryString.append(String.join(", ", rangeConditions));
            queryString.append(" } } }");
        }
        
        queryString.append("\n    ]\n");
        queryString.append("  }\n");
        queryString.append("}");
        
        // 构建分页查询
        Query searchQuery = StringQuery.builder(queryString.toString())
                .withPageable(PageRequest.of(request.getPageNum() - 1, request.getPageSize()))
                .build();
        
        // 执行搜索
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(searchQuery, ProductDocument.class);
        
        // 转换结果
        List<ProductSearchResponse> items = searchHits.getSearchHits().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        // 保存搜索历史
        if (request.getUserId() != null) {
            saveSearchHistory(request.getUserId(), request.getKeyword(), "KEYWORD", (int) searchHits.getTotalHits());
        }
        
        // 更新热门关键词
        updateHotKeyword(request.getKeyword());
        
        // 构建分页信息
        return ProductSearchResponse.PageInfo.builder()
                .total(searchHits.getTotalHits())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .totalPages((int) Math.ceil((double) searchHits.getTotalHits() / request.getPageSize()))
                .items(items)
                .build();
    }
    
    @Override
    public ProductSearchResponse searchByProductLink(String productUrl, Long userId) {
        log.info("商品链接搜索: url={}", productUrl);
        
        // 解析商品链接，提取平台和商品ID
        String[] parsed = parseProductUrl(productUrl);
        String platform = parsed[0];
        String productId = parsed[1];
        
        if (platform == null || productId == null) {
            throw new BusinessException("无法识别商品链接");
        }
        
        // 先从Elasticsearch查询
        ProductDocument document = productSearchRepository.findByPlatformAndPlatformProductId(platform, productId);
        
        if (document != null) {
            // 保存搜索历史
            if (userId != null) {
                saveSearchHistory(userId, productUrl, "LINK", 1);
            }
            
            return convertToResponse(new SearchHit<>(null, null, null, 1.0f, null, null, null, null, null, null, document));
        }
        
        // 如果ES中没有，调用商品服务解析并索引
        log.info("ES中未找到商品，调用商品服务解析: platform={}, productId={}", platform, productId);
        // TODO: 调用商品服务解析商品
        throw new BusinessException("商品未找到，请稍后再试");
    }
    
    @Override
    public ProductSearchResponse.PageInfo searchByImage(ImageSearchRequest request) {
        log.info("图片搜索: imageUrl={}", request.getImageUrl());
        
        // TODO: 实现图片搜索逻辑（需要使用图像识别服务）
        // 1. 调用图像识别服务提取图片特征
        // 2. 在Elasticsearch中搜索相似商品
        // 3. 返回相似度最高的商品列表
        
        throw new BusinessException("图片搜索功能开发中");
    }
    
    @Override
    public ProductSearchResponse searchByTaoCommand(TaoCommandSearchRequest request) {
        log.info("淘口令搜索: taoCommand={}", request.getTaoCommand());
        
        // 解析淘口令，提取商品ID
        String productId = parseTaoCommand(request.getTaoCommand());
        
        if (productId == null) {
            throw new BusinessException("无法识别淘口令");
        }
        
        // 按商品链接搜索
        String productUrl = "https://item.taobao.com/item.htm?id=" + productId;
        return searchByProductLink(productUrl, request.getUserId());
    }
    
    @Override
    public List<String> getHotKeywords(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 20;
        }
        
        LambdaQueryWrapper<HotKeyword> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HotKeyword::getVisible, true)
                .orderByDesc(HotKeyword::getWeight)
                .orderByDesc(HotKeyword::getSearchCount)
                .last("LIMIT " + limit);
        
        return hotKeywordMapper.selectList(wrapper).stream()
                .map(HotKeyword::getKeyword)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getSearchHistory(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 50;
        }
        
        LambdaQueryWrapper<SearchHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SearchHistory::getUserId, userId)
                .orderByDesc(SearchHistory::getCreateTime)
                .last("LIMIT " + limit);
        
        return searchHistoryMapper.selectList(wrapper).stream()
                .map(SearchHistory::getKeyword)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearSearchHistory(Long userId) {
        LambdaUpdateWrapper<SearchHistory> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SearchHistory::getUserId, userId)
                .set(SearchHistory::getIsDeleted, 1);
        
        searchHistoryMapper.update(null, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSearchHistory(Long userId, String keyword) {
        LambdaUpdateWrapper<SearchHistory> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SearchHistory::getUserId, userId)
                .eq(SearchHistory::getKeyword, keyword)
                .set(SearchHistory::getIsDeleted, 1);
        
        searchHistoryMapper.update(null, wrapper);
    }
    
    @Override
    public void indexProduct(Long productSnapshotId) {
        // TODO: 调用商品服务获取商品详情并索引到ES
        log.info("索引商品: productSnapshotId={}", productSnapshotId);
    }
    
    @Override
    public void batchIndexProducts(List<Long> productSnapshotIds) {
        // TODO: 批量索引商品
        log.info("批量索引商品: count={}", productSnapshotIds.size());
    }
    
    /**
     * 保存搜索历史
     */
    private void saveSearchHistory(Long userId, String keyword, String searchType, Integer resultCount) {
        try {
            SearchHistory history = new SearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            history.setSearchType(searchType);
            history.setResultCount(resultCount);
            
            searchHistoryMapper.insert(history);
        } catch (Exception e) {
            log.error("保存搜索历史失败: userId={}, keyword={}", userId, keyword, e);
        }
    }
    
    /**
     * 更新热门关键词
     */
    private void updateHotKeyword(String keyword) {
        try {
            LambdaQueryWrapper<HotKeyword> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HotKeyword::getKeyword, keyword);
            
            HotKeyword hotKeyword = hotKeywordMapper.selectOne(wrapper);
            
            if (hotKeyword != null) {
                // 更新搜索次数
                hotKeyword.setSearchCount(hotKeyword.getSearchCount() + 1);
                hotKeywordMapper.updateById(hotKeyword);
            } else {
                // 新增热门关键词
                hotKeyword = new HotKeyword();
                hotKeyword.setKeyword(keyword);
                hotKeyword.setSearchCount(1L);
                hotKeyword.setWeight(0);
                hotKeyword.setVisible(true);
                hotKeywordMapper.insert(hotKeyword);
            }
        } catch (Exception e) {
            log.error("更新热门关键词失败: keyword={}", keyword, e);
        }
    }
    
    /**
     * 解析商品链接
     */
    private String[] parseProductUrl(String url) {
        String platform = null;
        String productId = null;
        
        // 淘宝/天猫
        if (url.contains("taobao.com") || url.contains("tmall.com")) {
            platform = url.contains("tmall.com") ? "TMALL" : "TAOBAO";
            Pattern pattern = Pattern.compile("id=(\\d+)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                productId = matcher.group(1);
            }
        }
        // 京东
        else if (url.contains("jd.com")) {
            platform = "JD";
            Pattern pattern = Pattern.compile("/(\\d+)\\.html");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                productId = matcher.group(1);
            }
        }
        // 1688
        else if (url.contains("1688.com")) {
            platform = "ALIBABA_1688";
            Pattern pattern = Pattern.compile("offer/(\\d+)\\.html");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                productId = matcher.group(1);
            }
        }
        
        return new String[]{platform, productId};
    }
    
    /**
     * 解析淘口令
     */
    private String parseTaoCommand(String taoCommand) {
        // 淘口令格式：€XXXXX€ 或 ￥XXXXX￥
        Pattern pattern = Pattern.compile("[€￥]([A-Za-z0-9]+)[€￥]");
        Matcher matcher = pattern.matcher(taoCommand);
        
        if (matcher.find()) {
            String code = matcher.group(1);
            // TODO: 调用淘宝API解析淘口令获取商品ID
            log.info("解析淘口令: code={}", code);
        }
        
        return null;
    }
    
    /**
     * 转换为响应对象
     */
    private ProductSearchResponse convertToResponse(SearchHit<ProductDocument> hit) {
        ProductDocument doc = hit.getContent();
        
        return ProductSearchResponse.builder()
                .id(doc.getId())
                .platform(doc.getPlatform())
                .platformProductId(doc.getPlatformProductId())
                .title(doc.getTitle())
                .description(doc.getDescription())
                .price(doc.getPrice())
                .currency(doc.getCurrency())
                .mainImage(doc.getMainImage())
                .category(doc.getCategory())
                .brand(doc.getBrand())
                .salesCount(doc.getSalesCount())
                .reviewCount(doc.getReviewCount())
                .rating(doc.getRating())
                .productUrl(doc.getProductUrl())
                .snapshotTime(doc.getSnapshotTime())
                .score(hit.getScore())
                .build();
    }
}