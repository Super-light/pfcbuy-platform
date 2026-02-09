# 物流服务核心文件清单

## 已创建
✅ pom.xml
✅ application.yml  
✅ LogisticsServiceApplication.java

## 待创建（核心功能）
1. 实体类
   - ShippingOrder（物流订单）
   - TrackingInfo（物流追踪）
   - ShippingRoute（物流线路）
   - ShippingFee（运费配置）

2. 服务接口
   - LogisticsService（物流服务）
   - TrackingService（追踪服务）
   - ShippingFeeService（运费计算服务）

3. 物流渠道实现
   - DHLLogisticsProvider
   - FedExLogisticsProvider
   - UPSLogisticsProvider
   - EMSLogisticsProvider

4. Controller
   - LogisticsController
   - TrackingController

由于时间关系，建议采用**简化版实现**，核心API先定义好接口，后续对接真实物流API。
