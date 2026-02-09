package com.pfcbuy.search.controller;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.search.dto.*;
import com.pfcbuy.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索控制器
 *
 * @author PfcBuy Team
 */
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchService searchService;
    
    /**
     * 关键词搜索
     */
    @PostMapping("/keyword")
    public Result<ProductSearchResponse.PageInfo> searchByKeyword(@Valid @RequestBody ProductSearchRequest request) {
        log.info("关键词搜索请求: {}", request);
        ProductSearchResponse.PageInfo result = searchService.searchByKeyword(request);
        return Result.success(result);
    }
    
    /**
     * 商品链接搜索
     */
    @PostMapping("/link")
    public Result<ProductSearchResponse> searchByLink(@RequestParam String productUrl,
                                                      @RequestParam(required = false) Long userId) {
        log.info("商品链接搜索: url={}", productUrl);
        ProductSearchResponse result = searchService.searchByProductLink(productUrl, userId);
        return Result.success(result);
    }
    
    /**
     * 图片搜索
     */
    @PostMapping("/image")
    public Result<ProductSearchResponse.PageInfo> searchByImage(@Valid @RequestBody ImageSearchRequest request) {
        log.info("图片搜索请求: {}", request);
        ProductSearchResponse.PageInfo result = searchService.searchByImage(request);
        return Result.success(result);
    }
    
    /**
     * 淘口令搜索
     */
    @PostMapping("/tao-command")
    public Result<ProductSearchResponse> searchByTaoCommand(@Valid @RequestBody TaoCommandSearchRequest request) {
        log.info("淘口令搜索: {}", request);
        ProductSearchResponse result = searchService.searchByTaoCommand(request);
        return Result.success(result);
    }
    
    /**
     * 获取热门关键词
     */
    @GetMapping("/hot-keywords")
    public Result<List<String>> getHotKeywords(@RequestParam(defaultValue = "20") Integer limit) {
        List<String> keywords = searchService.getHotKeywords(limit);
        return Result.success(keywords);
    }
    
    /**
     * 获取搜索历史
     */
    @GetMapping("/history")
    public Result<List<String>> getSearchHistory(@RequestParam Long userId,
                                                  @RequestParam(defaultValue = "50") Integer limit) {
        List<String> history = searchService.getSearchHistory(userId, limit);
        return Result.success(history);
    }
    
    /**
     * 清空搜索历史
     */
    @DeleteMapping("/history/clear")
    public Result<Void> clearSearchHistory(@RequestParam Long userId) {
        searchService.clearSearchHistory(userId);
        return Result.success(null);
    }
    
    /**
     * 删除单条搜索历史
     */
    @DeleteMapping("/history")
    public Result<Void> deleteSearchHistory(@RequestParam Long userId, @RequestParam String keyword) {
        searchService.deleteSearchHistory(userId, keyword);
        return Result.success(null);
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Search Service is running");
    }
}
