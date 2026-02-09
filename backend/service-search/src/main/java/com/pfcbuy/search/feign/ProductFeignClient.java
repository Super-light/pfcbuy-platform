package com.pfcbuy.search.feign;

import com.pfcbuy.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务Feign客户端
 *
 * @author PfcBuy Team
 */
@FeignClient(name = "service-product", path = "/product")
public interface ProductFeignClient {
    
    /**
     * 根据商品快照ID获取商品详情
     */
    @GetMapping("/snapshot/{snapshotId}")
    Result<Object> getProductSnapshot(@PathVariable("snapshotId") Long snapshotId);
    
    /**
     * 解析商品URL
     */
    @PostMapping("/resolve")
    Result<Object> resolveProduct(@RequestParam("productUrl") String productUrl);
}
