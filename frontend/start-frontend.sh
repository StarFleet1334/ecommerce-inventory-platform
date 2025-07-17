#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Frontend Startup Script${NC}"
echo -e "${BLUE}========================${NC}"
echo ""

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}❌ Docker is not running. Please start Docker first.${NC}"
        exit 1
    fi
}

# Function to check if services are running
check_services() {
    echo -e "${YELLOW}🔍 Checking backend services...${NC}"
    
    # Check inventory service
    if curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Inventory Service is running${NC}"
    else
        echo -e "${YELLOW}⚠️  Inventory Service is not running${NC}"
        echo -e "${YELLOW}   You may need to start backend services first:${NC}"
        echo -e "${YELLOW}   docker compose --profile dev up inventory-service${NC}"
    fi
}

# Function to start development frontend
start_dev() {
    echo -e "${GREEN}🚀 Starting Development Frontend...${NC}"
    echo -e "${YELLOW}   Features: Hot reload, Debug panel, Source maps${NC}"
    echo ""
    
    check_docker
    check_services
    
    echo -e "${YELLOW}📦 Building development container...${NC}"
    docker compose build frontend-dev
    
    echo -e "${YELLOW}🌐 Starting development frontend...${NC}"
    docker compose --profile dev up -d frontend-dev
    
    echo ""
    echo -e "${GREEN}✅ Development Frontend Started!${NC}"
    echo -e "${GREEN}🌐 Access at: http://localhost:5173${NC}"
    echo -e "${YELLOW}📝 View logs: docker compose logs -f frontend-dev${NC}"
    echo -e "${YELLOW}🛑 Stop: docker compose stop frontend-dev${NC}"
}

# Function to start production frontend
start_prod() {
    echo -e "${GREEN}🚀 Starting Production Frontend...${NC}"
    echo -e "${YELLOW}   Features: Optimized build, Nginx, Health checks${NC}"
    echo ""
    
    check_docker
    check_services
    
    echo -e "${YELLOW}📦 Building production container...${NC}"
    docker compose build frontend
    
    echo -e "${YELLOW}🌐 Starting production frontend...${NC}"
    docker compose --profile prod up -d frontend
    
    echo ""
    echo -e "${GREEN}✅ Production Frontend Started!${NC}"
    echo -e "${GREEN}🌐 Access at: http://localhost:3000${NC}"
    echo -e "${YELLOW}📝 View logs: docker compose logs -f frontend${NC}"
    echo -e "${YELLOW}🛑 Stop: docker compose stop frontend${NC}"
}

# Function to start both frontends
start_both() {
    echo -e "${GREEN}🚀 Starting Both Frontends...${NC}"
    echo ""
    
    check_docker
    check_services
    
    echo -e "${YELLOW}📦 Building both containers...${NC}"
    docker compose build frontend frontend-dev
    
    echo -e "${YELLOW}🌐 Starting both frontends...${NC}"
    docker compose --profile dev up -d frontend frontend-dev
    
    echo ""
    echo -e "${GREEN}✅ Both Frontends Started!${NC}"
    echo -e "${GREEN}🌐 Development: http://localhost:5173${NC}"
    echo -e "${GREEN}🌐 Production: http://localhost:3000${NC}"
    echo -e "${YELLOW}📝 View logs: docker compose logs -f frontend frontend-dev${NC}"
    echo -e "${YELLOW}🛑 Stop: docker compose stop frontend frontend-dev${NC}"
}

# Function to show status
show_status() {
    echo -e "${BLUE}📊 Frontend Status${NC}"
    echo -e "${BLUE}================${NC}"
    echo ""
    
    # Check development frontend
    if docker ps --filter "name=frontend-dev" --format "table {{.Names}}\t{{.Status}}" | grep -q frontend-dev; then
        echo -e "${GREEN}✅ Development Frontend (5173): Running${NC}"
    else
        echo -e "${RED}❌ Development Frontend (5173): Not running${NC}"
    fi
    
    # Check production frontend
    if docker ps --filter "name=frontend" --format "table {{.Names}}\t{{.Status}}" | grep -q frontend; then
        echo -e "${GREEN}✅ Production Frontend (3000): Running${NC}"
    else
        echo -e "${RED}❌ Production Frontend (3000): Not running${NC}"
    fi
    
    echo ""
    echo -e "${YELLOW}🔍 Backend Services:${NC}"
    
    # Check inventory service
    if curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Inventory Service (8081): Running${NC}"
    else
        echo -e "${RED}❌ Inventory Service (8081): Not running${NC}"
    fi
}

# Function to show help
show_help() {
    echo -e "${BLUE}Frontend Startup Options${NC}"
    echo -e "${BLUE}=======================${NC}"
    echo ""
    echo -e "${GREEN}Usage: $0 [option]${NC}"
    echo ""
    echo -e "${YELLOW}Options:${NC}"
    echo -e "  ${GREEN}dev${NC}     - Start development frontend (port 5173)"
    echo -e "  ${GREEN}prod${NC}    - Start production frontend (port 3000)"
    echo -e "  ${GREEN}both${NC}    - Start both frontends"
    echo -e "  ${GREEN}status${NC}  - Show current status"
    echo -e "  ${GREEN}help${NC}    - Show this help message"
    echo ""
    echo -e "${YELLOW}Examples:${NC}"
    echo -e "  ${GREEN}$0 dev${NC}   - Start development frontend"
    echo -e "  ${GREEN}$0 prod${NC}  - Start production frontend"
    echo -e "  ${GREEN}$0 both${NC}  - Start both frontends"
    echo ""
    echo -e "${YELLOW}Recommendations:${NC}"
    echo -e "  • Use ${GREEN}dev${NC} for development and testing"
    echo -e "  • Use ${GREEN}prod${NC} for production deployment"
    echo -e "  • Use ${GREEN}both${NC} to compare both versions"
}

# Main script logic
case "${1:-help}" in
    "dev")
        start_dev
        ;;
    "prod")
        start_prod
        ;;
    "both")
        start_both
        ;;
    "status")
        show_status
        ;;
    "help"|*)
        show_help
        ;;
esac 