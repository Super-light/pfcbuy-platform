package com.pfcbuy.warehouse.service;

import com.pfcbuy.warehouse.dto.*;

import java.util.List;

/**
 * 包裹服务接口
 *
 * @author PfcBuy Team
 */
public interface PackageService {
    
    /**
     * 包裹入库
     */
    PackageResponse inbound(PackageInboundRequest request);
    
    /**
     * 包裹质检
     */
    PackageResponse qualityCheck(QualityCheckRequest request);
    
    /**
     * 包裹合并
     */
    PackageResponse mergePackages(PackageMergeRequest request);
    
    /**
     * 包裹出库
     */
    PackageResponse outbound(String packageNo);
    
    /**
     * 获取包裹详情
     */
    PackageResponse getPackageByNo(String packageNo);
    
    /**
     * 获取用户包裹列表
     */
    List<PackageResponse> getUserPackages(Long userId, String status);
    
    /**
     * 计算仓储费
     */
    void calculateStorageFee(String packageNo);
    
    /**
     * 上传质检照片
     */
    void uploadQcPhoto(String packageNo, List<String> photoUrls);
}
