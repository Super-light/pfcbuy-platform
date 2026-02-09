#!/bin/bash

cd "$(dirname "$0")"

if [ -z "$1" ]; then
    echo "📋 显示所有服务日志..."
    docker compose logs -f
else
    echo "📋 显示 $1 服务日志..."
    docker compose logs -f "$1"
fi
