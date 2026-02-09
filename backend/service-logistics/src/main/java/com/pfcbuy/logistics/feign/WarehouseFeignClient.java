package com.pfcbuy.logistics.feign;

import com.pfcbuy.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 仓储服务Feign客户端
 *
 * @author PfcBuy Team
 */
@FeignClient(name = "service-warehouse", path = "/api/warehouse")
public interface WarehouseFeignClient {

    /**
     * 根据包裹号获取包裹信息
     */
    @GetMapping("/packages/by-package-no/{packageNo}")
    Result<PackageDTO> getPackageByNo(@PathVariable("packageNo") String packageNo);

    /**
     * 更新包裹状态
     */
    @PostMapping("/packages/{packageNo}/status")
    Result<Void> updatePackageStatus(@PathVariable("packageNo") String packageNo,
                                      @RequestParam("status") String status);
}
