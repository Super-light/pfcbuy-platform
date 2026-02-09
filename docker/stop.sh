#!/bin/bash

echo "========================================="
echo "   PfcBuy Platform - Docker环境停止"
echo "========================================="
echo ""

cd "$(dirname "$0")"

echo "🛑 停止所有服务..."
docker compose down

echo ""
echo "✅ 所有服务已停止"
echo ""
echo "💡 提示："
echo "  重新启动: ./start.sh"
echo "  删除数据: docker compose down -v"
echo "========================================="
