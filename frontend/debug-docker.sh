#!/bin/bash

echo "🔍 Docker Frontend Debug Script"
echo "================================"

# Check if containers are running
echo "📋 Checking container status..."
docker ps --filter "name=frontend" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "📋 Checking container logs..."
echo "Frontend container logs:"
docker logs frontend --tail 20

echo ""
echo "🔍 Testing API connectivity from inside container..."
docker exec frontend curl -f http://inventory-service:8081/actuator/health || echo "❌ Cannot reach inventory service"

echo ""
echo "🌐 Testing frontend accessibility..."
curl -f http://localhost:3000/ || echo "❌ Frontend not accessible on port 3000"

echo ""
echo "🔧 Checking nginx configuration..."
docker exec frontend nginx -t

echo ""
echo "📁 Checking built files..."
docker exec frontend ls -la /usr/share/nginx/html/

echo ""
echo "🔍 Testing API proxy..."
curl -f http://localhost:3000/api/inventory/actuator/health || echo "❌ API proxy not working"

echo ""
echo "📊 Container resource usage:"
docker stats frontend --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}"

echo ""
echo "✅ Debug complete. Check the output above for issues." 