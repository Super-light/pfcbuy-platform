#!/bin/bash

echo "========================================="
echo "   PfcBuy Platform - 环境检查"
echo "========================================="
echo ""

# 检查Docker
echo "🔍 检查 Docker..."
if command -v docker &> /dev/null; then
    docker_version=$(docker --version)
    echo "✅ Docker已安装: $docker_version"
else
    echo "❌ Docker未安装"
    echo "   安装指南: https://docs.docker.com/get-docker/"
    exit 1
fi

# 检查Docker Compose
echo ""
echo "🔍 检查 Docker Compose..."
if command -v docker-compose &> /dev/null; then
    compose_version=$(docker-compose --version)
    echo "✅ Docker Compose已安装: $compose_version"
elif docker compose version &> /dev/null; then
    compose_version=$(docker compose version)
    echo "✅ Docker Compose已安装: $compose_version"
else
    echo "❌ Docker Compose未安装"
    exit 1
fi

# 检查Docker是否运行
echo ""
echo "🔍 检查 Docker服务状态..."
if docker info &> /dev/null; then
    echo "✅ Docker服务运行中"
else
    echo "❌ Docker服务未运行，请启动Docker Desktop"
    exit 1
fi

# 检查端口占用
echo ""
echo "🔍 检查端口占用..."

check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo "⚠️  端口 $1 已被占用"
        lsof -Pi :$1 -sTCP:LISTEN | tail -n +2
        return 1
    else
        echo "✅ 端口 $1 可用"
        return 0
    fi
}

all_ports_ok=true
check_port 3306 || all_ports_ok=false
check_port 6379 || all_ports_ok=false
check_port 9200 || all_ports_ok=false
check_port 8081 || all_ports_ok=false
check_port 8082 || all_ports_ok=false

# 检查配置文件
echo ""
echo "🔍 检查配置文件..."
cd "$(dirname "$0")"

if [ -f "docker-compose.yml" ]; then
    echo "✅ docker-compose.yml 存在"
else
    echo "❌ docker-compose.yml 缺失"
    exit 1
fi

sql_count=$(ls init-sql/*.sql 2>/dev/null | wc -l | xargs)
if [ "$sql_count" -gt 0 ]; then
    echo "✅ SQL初始化脚本: $sql_count 个"
else
    echo "❌ SQL初始化脚本缺失"
fi

# 总结
echo ""
echo "========================================="
if [ "$all_ports_ok" = true ]; then
    echo "✅ 环境检查通过，可以启动Docker环境"
    echo ""
    echo "💡 启动命令："
    echo "   ./start.sh"
else
    echo "⚠️  部分端口被占用，请先关闭占用端口的程序"
    echo "   或修改 docker-compose.yml 中的端口配置"
fi
echo "========================================="
