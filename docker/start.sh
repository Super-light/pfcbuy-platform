#!/bin/bash

echo "========================================="
echo "   PfcBuy Platform - Docker环境启动"
echo "========================================="
echo ""

# shellcheck disable=SC2164
cd "$(dirname "$0")"

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ Docker未安装，请先安装Docker"
    exit 1
fi

if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo "❌ Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

echo "启动基础服务（MySQL + Redis）..."
docker compose up -d mysql redis

echo ""
echo "等待服务启动（预计30秒）..."
sleep 10

echo ""
echo "🔍 检查服务状态..."
docker compose ps

echo ""
echo "========================================="
echo "   环境启动完成！"
echo "========================================="
echo ""
echo "📊 服务访问信息："
echo "  MySQL:   localhost:3306"
echo "    - 用户: root"
echo "    - 密码: root"
echo ""
echo "  Redis:   localhost:6379"
echo "    - 无密码"
echo ""
echo "💡 启动可选服务："
echo "  开发工具: docker compose --profile dev up -d"
echo "    - phpMyAdmin:  http://localhost:8082"
echo "    - Redis Commander: http://localhost:8081"
echo ""
echo "  完整环境: docker compose --profile full up -d"
echo "    - Elasticsearch: http://localhost:9200"
echo "    - Kibana: http://localhost:5601"
echo ""
echo "🛑 停止所有服务: ./stop.sh"
echo "========================================="
