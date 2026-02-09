package com.pfcbuy.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfcbuy.product.entity.ProductSnapshot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品快照Mapper接口
 * 
 * @author PfcBuy Team
 * @since 2024-02-03
 */
@Mapper
public interface ProductSnapshotMapper extends BaseMapper<ProductSnapshot> {

    /**
     * 根据平台和商品ID查询最新快照
     * 
     * @param platform 平台类型
     * @param platformProductId 平台商品ID
     * @return 最新快照
     */
    ProductSnapshot findLatestByPlatformAndProductId(
        @Param("platform") String platform,
        @Param("platformProductId") String platformProductId
    );
}
