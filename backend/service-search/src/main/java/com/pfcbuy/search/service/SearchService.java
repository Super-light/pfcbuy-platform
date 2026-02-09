package com.pfcbuy.search.service;

import com.pfcbuy.search.dto.*;

import java.util.List;

/**
 * 搜索服务接口
 *
 * @author PfcBuy Team
 */
public interface SearchService {
    
    /**
     * 关键词搜索商品
     */
    ProductSearchResponse.PageInfo searchByKeyword(ProductSearchRequest request);
    
    /**
     * 商品链接搜索
     */
    ProductSearchResponse searchByProductLink(String productUrl, Long userId);
    
    /**
     * 图片搜索
     */
    ProductSearchResponse.PageInfo searchByImage(ImageSearchRequest request);
    
    /**
     * 淘口令搜索
     */
    ProductSearchResponse searchByTaoCommand(TaoCommandSearchRequest request);
    
    /**
     * 获取热门关键词
     */
    List<String> getHotKeywords(Integer limit);
    
    /**
     * 获取搜索历史
     */
    List<String> getSearchHistory(Long userId, Integer limit);
    
    /**
     * 清空搜索历史
     */
    void clearSearchHistory(Long userId);
    
    /**
     * 删除单条搜索历史
     */
    void deleteSearchHistory(Long userId, String keyword);
    
    /**
     * 索引商品到Elasticsearch
     */
    void indexProduct(Long productSnapshotId);
    
    /**
     * 批量索引商品
     */
    void batchIndexProducts(List<Long> productSnapshotIds);
}
