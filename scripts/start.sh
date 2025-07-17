#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Starting E-commerce Inventory Platform...${NC}"

# Function to check if a port is open
check_port() {
    local host=$1
    local port=$2
    local max_attempts=$3
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if nc -z $host $port 2>/dev/null; then
            return 0
        fi
        echo -e "${YELLOW}Waiting for $host:$port... (attempt $attempt/$max_attempts)${NC}"
        sleep 2
        ((attempt++))
    done
    return 1
}

# Function to wait for service health
wait_for_service() {
    local service_name=$1
    local health_url=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}Waiting for $service_name to be healthy...${NC}"
    while [ $attempt -le $max_attempts ]; do
        if curl -f -s $health_url >/dev/null 2>&1; then
            echo -e "${GREEN}✅ $service_name is healthy!${NC}"
            return 0
        fi
        echo -e "${YELLOW}Waiting for $service_name... (attempt $attempt/$max_attempts)${NC}"
        sleep 3
        ((attempt++))
    done
    echo -e "${RED}❌ $service_name failed to become healthy${NC}"
    return 1
}

# Create necessary directories with proper permissions
echo -e "${YELLOW}📁 Creating log directories...${NC}"
sudo mkdir -p rocketmq/data/namesrv-logs/rocketmqlogs
sudo mkdir -p rocketmq/data/broker/logs/rocketmqlogs
sudo mkdir -p rocketmq/data/broker/store
sudo chmod -R 777 rocketmq/data

# Start RocketMQ services first
echo -e "${YELLOW}🚀 Starting RocketMQ services...${NC}"
docker compose --profile dev up -d namesrv

# Wait for namesrv to be ready (not necessarily healthy)
echo -e "${YELLOW}⏳ Waiting for RocketMQ namesrv to be ready...${NC}"
if check_port "localhost" "9876" 30; then
    echo -e "${GREEN}✅ RocketMQ namesrv is ready!${NC}"
else
    echo -e "${RED}❌ RocketMQ namesrv failed to start${NC}"
    exit 1
fi

# Start broker
echo -e "${YELLOW}🚀 Starting RocketMQ broker...${NC}"
docker compose --profile dev up -d broker

# Wait for broker to be ready
echo -e "${YELLOW}⏳ Waiting for RocketMQ broker to be ready...${NC}"
if check_port "localhost" "10911" 30; then
    echo -e "${GREEN}✅ RocketMQ broker is ready!${NC}"
else
    echo -e "${RED}❌ RocketMQ broker failed to start${NC}"
    exit 1
fi

# Start all other services
echo -e "${YELLOW}🚀 Starting all other services...${NC}"
docker compose --profile dev up -d

# Wait for services to be ready
echo -e "${YELLOW}⏳ Waiting for services to be ready...${NC}"

# Wait for inventory service
if wait_for_service "Inventory Service" "http://localhost:8081/swagger-ui/index.html"; then
    echo -e "${GREEN}✅ Inventory service is ready!${NC}"
else
    echo -e "${YELLOW}⚠️  Inventory service health check failed, but service might be running${NC}"
fi

# Wait for order processing service
if wait_for_service "Order Processing Service" "http://localhost:8083/swagger-ui/index.html"; then
    echo -e "${GREEN}✅ Order processing service is ready!${NC}"
else
    echo -e "${YELLOW}⚠️  Order processing service health check failed, but service might be running${NC}"
fi

# Wait for frontend
if wait_for_service "Frontend" "http://localhost:3000"; then
    echo -e "${GREEN}✅ Frontend is ready!${NC}"
else
    echo -e "${YELLOW}⚠️  Frontend health check failed, but service might be running${NC}"
fi

# Show final status
echo -e "${GREEN}🎉 All services started!${NC}"
echo -e "${GREEN}📊 Service URLs:${NC}"
echo -e "  Frontend: ${GREEN}http://localhost:3000${NC}"
echo -e "  Inventory Service: ${GREEN}http://localhost:8081${NC}"
echo -e "  Inventory Swagger: ${GREEN}http://localhost:8081/swagger-ui/index.html${NC}"
echo -e "  Order Processing: ${GREEN}http://localhost:8083${NC}"
echo -e "  Graphs Service: ${GREEN}http://localhost:8090${NC}"
echo -e "  Backend: ${GREEN}http://localhost:5000${NC}"
echo -e "  RocketMQ Dashboard: ${GREEN}http://localhost:8080${NC}"

echo -e "${YELLOW}📝 To view logs: docker compose logs -f${NC}"
echo -e "${YELLOW}🛑 To stop: docker compose down${NC}" 