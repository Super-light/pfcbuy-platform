package com.pfcbuy.order.feign;

import com.pfcbuy.common.result.Result;
import com.pfcbuy.order.dto.ProductSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品服务Feign客户端
 *
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@FeignClient(name = "service-product", url = "${service.product.url:http://localhost:8083}", path = "/api/v1/products")
public interface ProductFeignClient {

    /**
     * 根据快照ID查询商品快照
     *
     * @param snapshotId 快照ID
     * @return 商品快照
     */
    @GetMapping("/snapshot/{snapshotId}")
    Result<ProductSnapshotDTO> getProductSnapshot(@PathVariable("snapshotId") Long snapshotId);
}
