# Colors for output
$Red = "$([char]0x1b)[31m"
$Green = "$([char]0x1b)[32m"
$Yellow = "$([char]0x1b)[33m"
$Reset = "$([char]0x1b)[0m"

$Rocket = [char]::ConvertFromUtf32(0x1F680)      # 🚀
$Check = [char]::ConvertFromUtf32(0x2705)        # ✅
$Cross = [char]::ConvertFromUtf32(0x274C)        # ❌
$Warn = [char]::ConvertFromUtf32(0x26A0)         # ⚠️
$Folder = [char]::ConvertFromUtf32(0x1F4C1)      # 📁
$Hourglass = [char]::ConvertFromUtf32(0x23F3)    # ⏳
$Party = [char]::ConvertFromUtf32(0x1F389)       # 🎉
$Chart = [char]::ConvertFromUtf32(0x1F4CA)       # 📊
$Pencil = [char]::ConvertFromUtf32(0x1F4DD)      # 📝
$Stop = [char]::ConvertFromUtf32(0x1F6D1)        # 🛑

Write-Host "$Green$Rocket Starting E-commerce Inventory Platform...$Reset"

# Function to check if a port is open
function Test-Port {
    param(
        [string]$TargetHost,
        [int]$Port,
        [int]$MaxAttempts
    )

    $attempt = 1
    while ($attempt -le $MaxAttempts) {
        try {
            $tcpClient = New-Object System.Net.Sockets.TcpClient
            $tcpClient.ConnectAsync($TargetHost, $Port).Wait(2000) | Out-Null
            if ($tcpClient.Connected) {
                $tcpClient.Close()
                return $true
            }
            $tcpClient.Close()
        } catch {
            # Connection failed, continue
        }
        Write-Host "$Yellow Waiting for $TargetHost`:$Port... (attempt $attempt/$MaxAttempts)$Reset"
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

    Write-Host "$Yellow Waiting for $ServiceName to be healthy...$Reset"
    $attempt = 1
    while ($attempt -le $MaxAttempts) {
        try {
            $response = Invoke-WebRequest -Uri $HealthUrl -UseBasicParsing -TimeoutSec 5
            if ($response.StatusCode -eq 200) {
                Write-Host "$Green$Check $ServiceName is healthy!$Reset"
                return $true
            }
        } catch {
            # Service not ready yet
        }
        Write-Host "$Yellow Waiting for $ServiceName... (attempt $attempt/$MaxAttempts)$Reset"
        Start-Sleep -Seconds 3
        $attempt++
    }
    Write-Host "$Red$Cross $ServiceName failed to become healthy$Reset"
    return $false
}

$projectRoot = Split-Path -Parent $PSScriptRoot

$pathsToClean = @(
    "$projectRoot/rocketmq/data/broker/logs/rocketmqlogs",
    "$projectRoot/rocketmq/data/broker/store",
    "$projectRoot/rocketmq/data/namesrv/logs/rocketmqlogs",
    "$projectRoot/rocketmq/data/namesrv-logs/rocketmqlogs"
)

foreach ($path in $pathsToClean) {
    if (Test-Path $path) {
        Write-Host "$Yellow Removing files in $path …$Reset"
        try {
            Get-ChildItem -LiteralPath $path -Recurse -Force |
                Remove-Item -Recurse -Force -ErrorAction Stop
            Write-Host "$Green$Check Cleared $path$Reset"
        }
        catch {
            Write-Host "$Cross Failed to clean ${path}:`n$($_.Exception.Message)$Reset"
        }
    }
    else {
        Write-Host "$Warn Path not found (skipped): $path$Reset"
    }
}

Write-Host "$Green$Party RocketMQ folders cleaned!$Reset"

# Start RocketMQ services first
Write-Host "$Yellow$Rocket Starting RocketMQ services...$Reset"
docker compose --profile dev up -d namesrv --build

# Wait for namesrv to be ready (not necessarily healthy)
Write-Host "$Yellow$Hourglass Waiting for RocketMQ namesrv to be ready...$Reset"
if (Test-Port -TargetHost "localhost" -Port 9876 -MaxAttempts 30) {
    Write-Host "$Green$Check RocketMQ namesrv is ready!$Reset"
} else {
    Write-Host "$Red$Cross RocketMQ namesrv failed to start$Reset"
    exit 1
}

# Start broker
Write-Host "$Yellow$Rocket Starting RocketMQ broker...$Reset"
docker compose --profile dev up -d broker --build

# Wait for broker to be ready
Write-Host "$Yellow$Hourglass Waiting for RocketMQ broker to be ready...$Reset"
if (Test-Port -TargetHost "localhost" -Port 10911 -MaxAttempts 30) {
    Write-Host "$Green$Check RocketMQ broker is ready!$Reset"
} else {
    Write-Host "$Red$Cross RocketMQ broker failed to start$Reset"
    exit 1
}

Write-Host "$Yellow$Rocket Starting Api-GateWay...$Reset"
docker compose --profile dev up -d api-gateway --build
Write-Host "$Yellow$Hourglass Waiting for Api-GateWay to be ready...$Reset"


# Start all other services
Write-Host "$Yellow$Rocket Starting all other services...$Reset"
docker compose --profile dev up -d --build

# Wait for services to be ready - just check that ports are open
Write-Host "$Yellow$Hourglass Waiting for services to be ready...$Reset"

# Wait for inventory service port
if (Test-Port -TargetHost "localhost" -Port 8081 -MaxAttempts 30) {
    Write-Host "$Green$Check Inventory service port is open!$Reset"

    # Optional: Try health check with shorter timeout
    if (Wait-ForService -ServiceName "Inventory Service" -HealthUrl "http://localhost:8081/swagger-ui/index.html" -MaxAttempts 5) {
        Write-Host "$Green$Check Inventory service is fully ready!$Reset"
    } else {
        Write-Host "$Yellow$Warn Inventory service is starting but the UI might not be ready yet$Reset"
    }
} else {
    Write-Host "$Red$Cross Inventory service port failed to open$Reset"
}

# Wait for order processing service port
if (Test-Port -TargetHost "localhost" -Port 8083 -MaxAttempts 30) {
    Write-Host "$Green$Check Order processing service port is open!$Reset"

    # Optional: Try health check with shorter timeout
    if (Wait-ForService -ServiceName "Order Processing Service" -HealthUrl "http://localhost:8083/swagger-ui/index.html" -MaxAttempts 5) {
        Write-Host "$Green$Check Order processing service is fully ready!$Reset"
    } else {
        Write-Host "$Yellow$Warn Order processing service is starting but the UI might not be ready yet$Reset"
    }
} else {
    Write-Host "$Red$Cross Order processing service port failed to open$Reset"
}

# Wait for frontend services similarly
if (Test-Port -TargetHost "localhost" -Port 3000 -MaxAttempts 30) {
    Write-Host "$Green$Check Frontend (Production) port is open!$Reset"

    # Optional: Try health check with shorter timeout
    if (Wait-ForService -ServiceName "Frontend (Production)" -HealthUrl "http://localhost:3000" -MaxAttempts 5) {
        Write-Host "$Green$Check Frontend (Production) is fully ready!$Reset"
    } else {
        Write-Host "$Yellow$Warn Frontend (Production) is starting but might not be fully ready yet$Reset"
    }
} else {
    Write-Host "$Red$Cross Frontend (Production) port failed to open$Reset"
}

if (Test-Port -TargetHost "localhost" -Port 5173 -MaxAttempts 30) {
    Write-Host "$Green$Check Frontend (Development) port is open!$Reset"

    # Optional: Try health check with shorter timeout
    if (Wait-ForService -ServiceName "Frontend (Development)" -HealthUrl "http://localhost:5173" -MaxAttempts 5) {
        Write-Host "$Green$Check Frontend (Development) is fully ready!$Reset"
    } else {
        Write-Host "$Yellow$Warn Frontend (Development) is starting but might not be fully ready yet$Reset"
    }
} else {
    Write-Host "$Red$Cross Frontend (Development) port failed to open$Reset"
}

# Show final status
Write-Host "$Green$Party All services started!$Reset"
Write-Host "$Green$Chart Service URLs:$Reset"
Write-Host "  Frontend (Production): ${Green}http://localhost:3000${Reset}"
Write-Host "  Frontend (Development): ${Green}http://localhost:5173${Reset}"
Write-Host "  Inventory Service: ${Green}http://localhost:8081${Reset}"
Write-Host "  Inventory Swagger: ${Green}http://localhost:8081/swagger-ui/index.html${Reset}"
Write-Host "  Order Processing: ${Green}http://localhost:8083${Reset}"
Write-Host "  Graphs Service: ${Green}http://localhost:8090${Reset}"
Write-Host "  Backend: ${Green}http://localhost:5000${Reset}"
Write-Host "  RocketMQ Dashboard: ${Green}http://localhost:8080${Reset}"

Write-Host "$Yellow$Pencil To view logs: docker compose logs -f$Reset"
Write-Host "$Yellow$Stop To stop: docker compose down$Reset"