package com.pfcbuy.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pfcbuy.common.exception.BusinessException;
import com.pfcbuy.warehouse.dto.*;
import com.pfcbuy.warehouse.entity.Package;
import com.pfcbuy.warehouse.entity.PackageOperation;
import com.pfcbuy.warehouse.entity.QcPhoto;
import com.pfcbuy.warehouse.mapper.PackageMapper;
import com.pfcbuy.warehouse.mapper.PackageOperationMapper;
import com.pfcbuy.warehouse.mapper.QcPhotoMapper;
import com.pfcbuy.warehouse.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 包裹服务实现
 *
 * @author PfcBuy Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {
    
    private final PackageMapper packageMapper;
    private final QcPhotoMapper qcPhotoMapper;
    private final PackageOperationMapper operationMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PackageResponse inbound(PackageInboundRequest request) {
        log.info("包裹入库: orderNo={}", request.getOrderNo());
        
        // 生成包裹号
        String packageNo = generatePackageNo();
        
        // 计算体积重
        BigDecimal volumeWeight = calculateVolumeWeight(
            request.getLength(), 
            request.getWidth(), 
            request.getHeight()
        );
        
        // 创建包裹记录
        Package pkg = Package.builder()
                .packageNo(packageNo)
                .userId(request.getUserId())
                .orderNo(request.getOrderNo())
                .status("INBOUND")
                .expressCompany(request.getExpressCompany())
                .trackingNo(request.getTrackingNo())
                .senderName(request.getSenderName())
                .senderPhone(request.getSenderPhone())
                .productDescription(request.getProductDescription())
                .quantity(request.getQuantity())
                .declaredValue(request.getDeclaredValue())
                .currency(request.getCurrency())
                .actualWeight(request.getActualWeight())
                .volumeWeight(volumeWeight)
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .inboundTime(LocalDateTime.now())
                .storageDays(0)
                .storageFee(BigDecimal.ZERO)
                .needQc(request.getNeedQc())
                .qcStatus(request.getNeedQc() ? "PENDING" : "NOT_REQUIRED")
                .merged(false)
                .remark(request.getRemark())
                .build();
        
        packageMapper.insert(pkg);
        
        // 记录操作日志
        recordOperation(packageNo, "INBOUND", null, "INBOUND", "系统", "包裹入库");
        
        return convertToResponse(pkg);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PackageResponse qualityCheck(QualityCheckRequest request) {
        log.info("包裹质检: packageNo={}", request.getPackageNo());
        
        // 查询包裹
        Package pkg = packageMapper.selectOne(
            new LambdaQueryWrapper<Package>()
                .eq(Package::getPackageNo, request.getPackageNo())
        );
        
        if (pkg == null) {
            throw new BusinessException("包裹不存在");
        }
        
        if (!"INBOUND".equals(pkg.getStatus())) {
            throw new BusinessException("包裹状态不正确，无法质检");
        }
        
        // 更新质检状态
        packageMapper.update(null,
            new LambdaUpdateWrapper<Package>()
                .eq(Package::getPackageNo, request.getPackageNo())
                .set(Package::getQcStatus, request.getQcResult())
                .set(Package::getQcRemark, request.getQcRemark())
                .set(Package::getQcTime, LocalDateTime.now())
                .set(Package::getStatus, "QC_COMPLETED")
        );
        
        // 保存质检照片
        if (request.getPhotoUrls() != null && !request.getPhotoUrls().isEmpty()) {
            uploadQcPhoto(request.getPackageNo(), request.getPhotoUrls());
        }
        
        // 记录操作日志
        recordOperation(request.getPackageNo(), "QC", "INBOUND", "QC_COMPLETED", 
            "质检员", "质检完成: " + request.getQcResult());
        
        // 重新查询包裹
        pkg = packageMapper.selectOne(
            new LambdaQueryWrapper<Package>()
                .eq(Package::getPackageNo, request.getPackageNo())
        );
        
        return convertToResponse(pkg);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PackageResponse mergePackages(PackageMergeRequest request) {
        log.info("包裹合并: packageNos={}", request.getPackageNos());
        
        // 验证所有包裹是否属于同一用户且可合并
        List<Package> packages = packageMapper.selectList(
            new LambdaQueryWrapper<Package>()
                .in(Package::getPackageNo, request.getPackageNos())
                .eq(Package::getUserId, request.getUserId())
        );
        
        if (packages.size() != request.getPackageNos().size()) {
            throw new BusinessException("部分包裹不存在或不属于该用户");
        }
        
        for (Package pkg : packages) {
            if (!"QC_COMPLETED".equals(pkg.getStatus())) {
                throw new BusinessException("包裹 " + pkg.getPackageNo() + " 状态不正确，无法合并");
            }
        }
        
        // 生成合并后的包裹号
        String mergedPackageNo = generatePackageNo();
        
        // 计算合并后的重量和体积
        BigDecimal totalWeight = packages.stream()
            .map(Package::getActualWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalVolume = packages.stream()
            .map(Package::getVolumeWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 创建合并后的包裹
        Package mergedPackage = Package.builder()
                .packageNo(mergedPackageNo)
                .userId(request.getUserId())
                .orderNo(packages.get(0).getOrderNo())
                .status("MERGED")
                .productDescription("合并包裹")
                .quantity(packages.stream().mapToInt(Package::getQuantity).sum())
                .actualWeight(totalWeight)
                .volumeWeight(totalVolume)
                .inboundTime(LocalDateTime.now())
                .storageDays(0)
                .storageFee(BigDecimal.ZERO)
                .needQc(false)
                .qcStatus("NOT_REQUIRED")
                .merged(true)
                .remark(request.getRemark())
                .build();
        
        packageMapper.insert(mergedPackage);
        
        // 更新原包裹状态
        for (Package pkg : packages) {
            packageMapper.update(null,
                new LambdaUpdateWrapper<Package>()
                    .eq(Package::getPackageNo, pkg.getPackageNo())
                    .set(Package::getStatus, "MERGED")
                    .set(Package::getParentPackageNo, mergedPackageNo)
            );
            
            recordOperation(pkg.getPackageNo(), "MERGE", "QC_COMPLETED", "MERGED", 
                "系统", "合并到: " + mergedPackageNo);
        }
        
        // 记录合并操作
        recordOperation(mergedPackageNo, "MERGE", null, "MERGED", 
            "系统", "合并了 " + packages.size() + " 个包裹");
        
        return convertToResponse(mergedPackage);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PackageResponse outbound(String packageNo) {
        log.info("包裹出库: packageNo={}", packageNo);
        
        Package pkg = packageMapper.selectOne(
            new LambdaQueryWrapper<Package>()
                .eq(Package::getPackageNo, packageNo)
        );
        
        if (pkg == null) {
            throw new BusinessException("包裹不存在");
        }
        
        if (!"QC_COMPLETED".equals(pkg.getStatus()) && !"MERGED".equals(pkg.getStatus())) {
            throw new BusinessException("包裹状态不正确，无法出库");
        }
        
        // 计算仓储天数和费用
        calculateStorageFee(packageNo);
        
        // 更新出库状态
        packageMapper.update(null,
            new LambdaUpdateWrapper<Package>()
                .eq(Package::getPackageNo, packageNo)
                .set(Package::getStatus, "OUTBOUND")
                .set(Package::getOutboundTime, LocalDateTime.now())
        );
        
        // 记录操作日志
        recordOperation(packageNo, "OUTBOUND", pkg.getStatus(), "OUTBOUND", 
            "仓管员", "包裹出库");
        
        // 重新查询
        pkg = packageMapper.selectOne(
            new LambdaQueryWrapper<Package>()
                .eq(Package::getPackageNo, packageNo)
        );
        
        return convertToResponse(pkg);
    }
    
    @Override
    public PackageResponse getPackageByNo(String packageNo) {
        Package pkg = packageMapper.selectOne(
            new LambdaQueryWrapper<Package>()
                .eq(Package::getPackageNo, packageNo)
        );
        
        if (pkg == null) {
            throw new BusinessException("包裹不存在");
        }
        
        return convertToResponse(pkg);
    }
    
    @Override
    public List<PackageResponse> getUserPackages(Long userId, String status) {
        LambdaQueryWrapper<Package> wrapper = new LambdaQueryWrapper<Package>()
                .eq(Package::getUserId, userId)
                .orderByDesc(Package::getCreateTime);
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Package::getStatus, status);
        }
        
        List<Package> packages = packageMapper.selectList(wrapper);
        
        return packages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateStorageFee(String packageNo) {
        Package pkg = packageMapper.selectOne(
            new LambdaQueryWrapper<Package>()
                .eq(Package::getPackageNo, packageNo)
        );
        
        if (pkg == null || pkg.getInboundTime() == null) {
            return;
        }
        
        // 计算仓储天数
        long days = ChronoUnit.DAYS.between(pkg.getInboundTime(), LocalDateTime.now());
        
        // 计算仓储费（例如：前7天免费，之后每天1元/kg）
        BigDecimal storageFee = BigDecimal.ZERO;
        if (days > 7) {
            long chargeableDays = days - 7;
            storageFee = pkg.getActualWeight()
                    .multiply(BigDecimal.valueOf(chargeableDays))
                    .multiply(BigDecimal.ONE);
        }
        
        // 更新仓储信息
        packageMapper.update(null,
            new LambdaUpdateWrapper<Package>()
                .eq(Package::getPackageNo, packageNo)
                .set(Package::getStorageDays, (int) days)
                .set(Package::getStorageFee, storageFee)
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadQcPhoto(String packageNo, List<String> photoUrls) {
        int order = 1;
        for (String photoUrl : photoUrls) {
            QcPhoto photo = QcPhoto.builder()
                    .packageNo(packageNo)
                    .photoType("QC")
                    .photoUrl(photoUrl)
                    .sortOrder(order++)
                    .build();
            
            qcPhotoMapper.insert(photo);
        }
    }
    
    /**
     * 记录操作日志
     */
    private void recordOperation(String packageNo, String operationType, 
                                 String beforeStatus, String afterStatus,
                                 String operatorName, String description) {
        PackageOperation operation = PackageOperation.builder()
                .packageNo(packageNo)
                .operationType(operationType)
                .beforeStatus(beforeStatus)
                .afterStatus(afterStatus)
                .operatorName(operatorName)
                .description(description)
                .build();
        
        operationMapper.insert(operation);
    }
    
    /**
     * 生成包裹号
     */
    private String generatePackageNo() {
        return "PKG" + System.currentTimeMillis() + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * 计算体积重（长*宽*高/5000）
     */
    private BigDecimal calculateVolumeWeight(BigDecimal length, BigDecimal width, BigDecimal height) {
        if (length == null || width == null || height == null) {
            return BigDecimal.ZERO;
        }
        
        return length.multiply(width).multiply(height)
                .divide(BigDecimal.valueOf(5000), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * 转换为响应对象
     */
    private PackageResponse convertToResponse(Package pkg) {
        // 查询质检照片
        List<QcPhoto> photos = qcPhotoMapper.selectList(
            new LambdaQueryWrapper<QcPhoto>()
                .eq(QcPhoto::getPackageNo, pkg.getPackageNo())
                .orderByAsc(QcPhoto::getSortOrder)
        );
        
        List<QcPhotoResponse> photoResponses = photos.stream()
                .map(photo -> QcPhotoResponse.builder()
                        .id(photo.getId())
                        .packageNo(photo.getPackageNo())
                        .photoType(photo.getPhotoType())
                        .photoUrl(photo.getPhotoUrl())
                        .thumbnailUrl(photo.getThumbnailUrl())
                        .sortOrder(photo.getSortOrder())
                        .description(photo.getDescription())
                        .uploadUserId(photo.getUploadUserId())
                        .createTime(photo.getCreateTime())
                        .build())
                .collect(Collectors.toList());
        
        // 查询操作记录
        List<PackageOperation> operations = operationMapper.selectList(
            new LambdaQueryWrapper<PackageOperation>()
                .eq(PackageOperation::getPackageNo, pkg.getPackageNo())
                .orderByDesc(PackageOperation::getCreateTime)
        );
        
        List<PackageOperationResponse> operationResponses = operations.stream()
                .map(op -> PackageOperationResponse.builder()
                        .id(op.getId())
                        .packageNo(op.getPackageNo())
                        .operationType(op.getOperationType())
                        .beforeStatus(op.getBeforeStatus())
                        .afterStatus(op.getAfterStatus())
                        .operatorId(op.getOperatorId())
                        .operatorName(op.getOperatorName())
                        .description(op.getDescription())
                        .remark(op.getRemark())
                        .createTime(op.getCreateTime())
                        .build())
                .collect(Collectors.toList());
        
        return PackageResponse.builder()
                .id(pkg.getId())
                .packageNo(pkg.getPackageNo())
                .userId(pkg.getUserId())
                .orderNo(pkg.getOrderNo())
                .status(pkg.getStatus())
                .expressCompany(pkg.getExpressCompany())
                .trackingNo(pkg.getTrackingNo())
                .senderName(pkg.getSenderName())
                .senderPhone(pkg.getSenderPhone())
                .productDescription(pkg.getProductDescription())
                .quantity(pkg.getQuantity())
                .declaredValue(pkg.getDeclaredValue())
                .currency(pkg.getCurrency())
                .actualWeight(pkg.getActualWeight())
                .volumeWeight(pkg.getVolumeWeight())
                .length(pkg.getLength())
                .width(pkg.getWidth())
                .height(pkg.getHeight())
                .inboundTime(pkg.getInboundTime())
                .outboundTime(pkg.getOutboundTime())
                .storageDays(pkg.getStorageDays())
                .storageFee(pkg.getStorageFee())
                .needQc(pkg.getNeedQc())
                .qcStatus(pkg.getQcStatus())
                .qcRemark(pkg.getQcRemark())
                .qcTime(pkg.getQcTime())
                .parentPackageNo(pkg.getParentPackageNo())
                .merged(pkg.getMerged())
                .exceptionReason(pkg.getExceptionReason())
                .remark(pkg.getRemark())
                .createTime(pkg.getCreateTime())
                .updateTime(pkg.getUpdateTime())
                .qcPhotos(photoResponses)
                .operations(operationResponses)
                .build();
    }
}
