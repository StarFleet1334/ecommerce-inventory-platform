# Colors for output
$Red = "`e[31m"
$Green = "`e[32m"
$Yellow = "`e[33m"
$Reset = "`e[0m"

Write-Host "$Green🚀 Starting E-commerce Inventory Platform...$Reset"

# Function to check if a port is open
function Test-Port {
    param(
        [string]$Host,
        [int]$Port,
        [int]$MaxAttempts
    )
    
    $attempt = 1
    while ($attempt -le $MaxAttempts) {
        try {
            $tcpClient = New-Object System.Net.Sockets.TcpClient
            $tcpClient.ConnectAsync($Host, $Port).Wait(2000) | Out-Null
            if ($tcpClient.Connected) {
                $tcpClient.Close()
                return $true
            }
            $tcpClient.Close()
        } catch {
            # Connection failed, continue
        }
        Write-Host "$YellowWaiting for $Host`:$Port... (attempt $attempt/$MaxAttempts)$Reset"
        Start-Sleep -Seconds 2
        $attempt++
    }
    return $false
}

# Function to wait for service health
function Wait-ForService {
    param(
        [string]$ServiceName,
        [string]$HealthUrl,
        [int]$MaxAttempts = 30
    )
    
    Write-Host "$YellowWaiting for $ServiceName to be healthy...$Reset"
    $attempt = 1
    while ($attempt -le $MaxAttempts) {
        try {
            $response = Invoke-WebRequest -Uri $HealthUrl -UseBasicParsing -TimeoutSec 5
            if ($response.StatusCode -eq 200) {
                Write-Host "$Green✅ $ServiceName is healthy!$Reset"
                return $true
            }
        } catch {
            # Service not ready yet
        }
        Write-Host "$YellowWaiting for $ServiceName... (attempt $attempt/$MaxAttempts)$Reset"
        Start-Sleep -Seconds 3
        $attempt++
    }
    Write-Host "$Red❌ $ServiceName failed to become healthy$Reset"
    return $false
}

# Create necessary directories with proper permissions
Write-Host "$Yellow📁 Creating log directories...$Reset"
if (-not (Test-Path "rocketmq/data/namesrv-logs/rocketmqlogs")) {
    New-Item -ItemType Directory -Path "rocketmq/data/namesrv-logs/rocketmqlogs" -Force | Out-Null
}
if (-not (Test-Path "rocketmq/data/broker/logs/rocketmqlogs")) {
    New-Item -ItemType Directory -Path "rocketmq/data/broker/logs/rocketmqlogs" -Force | Out-Null
}
if (-not (Test-Path "rocketmq/data/broker/store")) {
    New-Item -ItemType Directory -Path "rocketmq/data/broker/store" -Force | Out-Null
}

# Start RocketMQ services first
Write-Host "$Yellow🚀 Starting RocketMQ services...$Reset"
docker compose --profile dev up -d namesrv

# Wait for namesrv to be ready (not necessarily healthy)
Write-Host "$Yellow⏳ Waiting for RocketMQ namesrv to be ready...$Reset"
if (Test-Port -Host "localhost" -Port 9876 -MaxAttempts 30) {
    Write-Host "$Green✅ RocketMQ namesrv is ready!$Reset"
} else {
    Write-Host "$Red❌ RocketMQ namesrv failed to start$Reset"
    exit 1
}

# Start broker
Write-Host "$Yellow🚀 Starting RocketMQ broker...$Reset"
docker compose --profile dev up -d broker

# Wait for broker to be ready
Write-Host "$Yellow⏳ Waiting for RocketMQ broker to be ready...$Reset"
if (Test-Port -Host "localhost" -Port 10911 -MaxAttempts 30) {
    Write-Host "$Green✅ RocketMQ broker is ready!$Reset"
} else {
    Write-Host "$Red❌ RocketMQ broker failed to start$Reset"
    exit 1
}

# Start all other services
Write-Host "$Yellow🚀 Starting all other services...$Reset"
docker compose --profile dev up -d

# Wait for services to be ready
Write-Host "$Yellow⏳ Waiting for services to be ready...$Reset"

# Wait for inventory service
if (Wait-ForService -ServiceName "Inventory Service" -HealthUrl "http://localhost:8081/swagger-ui/index.html") {
    Write-Host "$Green✅ Inventory service is ready!$Reset"
} else {
    Write-Host "$Yellow⚠️  Inventory service health check failed, but service might be running$Reset"
}

# Wait for order processing service
if (Wait-ForService -ServiceName "Order Processing Service" -HealthUrl "http://localhost:8083/swagger-ui/index.html") {
    Write-Host "$Green✅ Order processing service is ready!$Reset"
} else {
    Write-Host "$Yellow⚠️  Order processing service health check failed, but service might be running$Reset"
}

# Wait for frontend services
if (Wait-ForService -ServiceName "Frontend (Production)" -HealthUrl "http://localhost:3000") {
    Write-Host "$Green✅ Frontend (Production) is ready!$Reset"
} else {
    Write-Host "$Yellow⚠️  Frontend (Production) health check failed, but service might be running$Reset"
}

if (Wait-ForService -ServiceName "Frontend (Development)" -HealthUrl "http://localhost:5173") {
    Write-Host "$Green✅ Frontend (Development) is ready!$Reset"
} else {
    Write-Host "$Yellow⚠️  Frontend (Development) health check failed, but service might be running$Reset"
}

# Show final status
Write-Host "$Green🎉 All services started!$Reset"
Write-Host "$Green📊 Service URLs:$Reset"
Write-Host "  Frontend (Production): $Greenhttp://localhost:3000$Reset"
Write-Host "  Frontend (Development): $Greenhttp://localhost:5173$Reset"
Write-Host "  Inventory Service: $Greenhttp://localhost:8081$Reset"
Write-Host "  Inventory Swagger: $Greenhttp://localhost:8081/swagger-ui/index.html$Reset"
Write-Host "  Order Processing: $Greenhttp://localhost:8083$Reset"
Write-Host "  Graphs Service: $Greenhttp://localhost:8090$Reset"
Write-Host "  Backend: $Greenhttp://localhost:5000$Reset"
Write-Host "  RocketMQ Dashboard: $Greenhttp://localhost:8080$Reset"

Write-Host "$Yellow📝 To view logs: docker compose logs -f$Reset"
Write-Host "$Yellow🛑 To stop: docker compose down$Reset"