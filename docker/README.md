# PfcBuy Platform - Docker环境使用指南

## 环境说明

本Docker环境提供PfcBuy平台所需的所有基础服务，包括：

### 核心服务（默认启动）
- **MySQL 8.0**: 主数据库，端口3306
- **Redis 7**: 缓存服务，端口6379

### 可选服务
- **Elasticsearch 8**: 搜索引擎，端口9200（使用 `--profile full` 启动）
- **Kibana**: ES可视化界面，端口5601（使用 `--profile full` 启动）
- **phpMyAdmin**: MySQL管理工具，端口8082（使用 `--profile dev` 启动）
- **Redis Commander**: Redis管理工具，端口8081（使用 `--profile dev` 启动）

---

## 快速开始

### 1. 启动基础环境（MySQL + Redis）

```bash
cd docker
./start.sh
```

或者使用docker compose命令：
```bash
docker compose up -d
```

### 2. 启动开发环境（含管理工具）

```bash
docker compose --profile dev up -d
```

启动后可访问：
- phpMyAdmin: http://localhost:8082
- Redis Commander: http://localhost:8081

### 3. 启动完整环境（含Elasticsearch）

```bash
docker compose --profile full --profile dev up -d
```

---

## 停止服务

### 停止所有服务
```bash
./stop.sh
```

或
```bash
docker compose down
```

### 停止并删除数据卷
```bash
docker compose down -v
```

**警告**: 此操作会删除所有数据！

---

## 查看日志

### 查看所有服务日志
```bash
./logs.sh
```

### 查看指定服务日志
```bash
./logs.sh mysql
./logs.sh redis
```

或直接使用docker compose：
```bash
docker compose logs -f mysql
```

---

## 检查服务状态

```bash
docker compose ps
```

---

## 数据库连接信息

### MySQL
- **Host**: localhost
- **Port**: 3306
- **User**: root
- **Password**: root
- **已创建数据库**: 
  - pfcbuy_user (用户服务)
  - pfcbuy_product (商品服务)
  - pfcbuy_order (订单服务)
  - pfcbuy_payment (支付服务)
  - pfcbuy_search (搜索服务)
  - pfcbuy_warehouse (仓储服务)
  - pfcbuy_logistics (物流服务)
  - pfcbuy_gateway (网关服务)

### Redis
- **Host**: localhost
- **Port**: 6379
- **Password**: 无

---

## 目录结构

```
docker/
├── docker-compose.yml      # Docker Compose配置
├── start.sh                # 启动脚本
├── stop.sh                 # 停止脚本
├── logs.sh                 # 日志查看脚本
├── mysql-conf/             # MySQL配置文件
│   └── my.cnf
├── redis-conf/             # Redis配置文件
│   └── redis.conf
└── init-sql/               # 数据库初始化脚本
    ├── 01-init-databases.sql
    ├── 02-user-tables.sql
    ├── 03-product-tables.sql
    ├── 04-order-tables.sql
    ├── 05-payment-tables.sql
    ├── 06-search-tables.sql
    ├── 07-warehouse-tables.sql
    ├── 08-logistics-tables.sql
    └── 09-gateway-tables.sql
```

---

## 常见问题

### 1. 端口被占用
如果3306或6379端口被占用，修改 `docker-compose.yml` 中的端口映射：
```yaml
ports:
  - "13306:3306"  # 改为其他端口
```

### 2. 数据库初始化失败
查看MySQL日志：
```bash
docker compose logs mysql
```

重新初始化（删除数据卷后重启）：
```bash
docker compose down -v
docker compose up -d
```

### 3. 服务无法连接
检查服务健康状态：
```bash
docker compose ps
```

查看具体服务日志：
```bash
docker compose logs -f mysql
```

### 4. 内存不足
如果机器内存有限，可以调整 `docker-compose.yml` 中的资源配置：
- 降低Elasticsearch内存：`ES_JAVA_OPTS=-Xms256m -Xmx256m`
- 降低MySQL buffer pool：修改 `mysql-conf/my.cnf`

---

## 清理环境

### 清理停止的容器
```bash
docker compose rm
```

### 清理未使用的镜像
```bash
docker image prune -a
```

### 完全清理（包括数据）
```bash
docker compose down -v
docker system prune -a --volumes
```

---

## 开发建议

1. **开发阶段**: 使用 `--profile dev` 启动管理工具，方便调试
2. **测试阶段**: 使用基础环境即可，减少资源占用
3. **生产环境**: 不要使用此配置，应该使用独立的生产配置

---

**维护者:** plus  
**更新日期:** 2024-02-06
