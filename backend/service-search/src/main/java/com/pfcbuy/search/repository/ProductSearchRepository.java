package com.pfcbuy.search.repository;

import com.pfcbuy.search.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品搜索Repository（Elasticsearch）
 *
 * @author PfcBuy Team
 */
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    
    /**
     * 根据平台查询商品
     */
    List<ProductDocument> findByPlatform(String platform);
    
    /**
     * 根据平台和商品ID查询
     */
    ProductDocument findByPlatformAndPlatformProductId(String platform, String platformProductId);
}
