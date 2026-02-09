# PfcBuy Platform - 跨境代购&转运平台

一个基于 Java Spring Boot + React 的全球购物与转运商城系统。

## 项目结构

```
pfcbuy-platform/
├── backend/                    # Java后端微服务
│   ├── common/                # 公共模块
│   ├── gateway/               # API网关（端口8080）
│   ├── service-user/          # 用户服务（端口8084）
│   ├── service-product/       # 商品解析服务（端口8083）
│   ├── service-order/         # 订单服务（端口8081）
│   ├── service-payment/       # 支付服务（端口8082）
│   ├── service-search/        # 搜索服务（端口8085）
│   ├── service-warehouse/     # 仓储服务（端口8086）
│   └── service-logistics/     # 物流服务（端口8087）
├── frontend/                   # React前端（端口3000）
└── docker/                     # Docker配置
    ├── docker-compose.yml     # Docker编排文件
    └── init-sql/              # 数据库初始化脚本（9个SQL文件）
```

## 快速启动

### 前置要求
- Java 17+
- Maven 3.8+
- Node.js 18+
- Docker & Docker Compose
- MySQL 8.0+
- Redis 6.0+

### 1. 启动基础设施（MySQL + Redis）
```bash
cd docker
docker-compose up -d
```

### 2. 初始化数据库
```bash
# MySQL会自动执行docker/init-sql/下的所有SQL脚本
# 包含8个数据库，32张表的创建
```

### 3. 启动后端服务
```bash
cd backend
mvn clean install

# 启动各个微服务（示例）
cd service-user && mvn spring-boot:run &
cd service-product && mvn spring-boot:run &
cd service-order && mvn spring-boot:run &
cd service-payment && mvn spring-boot:run &
# ... 其他服务
```

### 4. 启动前端
```bash
cd frontend
npm install
npm run dev
```

访问：http://localhost:3000

## 数据库说明

项目包含 **8个独立数据库**，共 **32张数据表**：

| 数据库              | 说明   | 表数量 |
|------------------|------|-----|
| pfcbuy_user      | 用户服务 | 3   |
| pfcbuy_product   | 商品服务 | 2   |
| pfcbuy_order     | 订单服务 | 3   |
| pfcbuy_payment   | 支付服务 | 4   |
| pfcbuy_search    | 搜索服务 | 4   |
| pfcbuy_warehouse | 仓储服务 | 4   |
| pfcbuy_logistics | 物流服务 | 4   |
| pfcbuy_gateway   | 网关服务 | 8   |

## 支付渠道

- Stripe（国际信用卡）
- 飞来汇（多渠道支付）

## 开发状态

- 项目架构搭建完成
- 数据库设计完成（8个库，32张表）
- 微服务代码框架完成（9个模块）
- Mapper XML配置完成
- TODO 业务逻辑完善中
- TODO 前端页面开发中
- TODO API网关路由配置中

## 技术栈

### 后端
- Spring Boot 3.2.x
- Spring Cloud Gateway
- MyBatis-Plus
- MySQL 8.0
- Redis
- Stripe SDK
- JWT

### 前端
- React 18
- TypeScript
- Vite
- Ant Design
- React Router
