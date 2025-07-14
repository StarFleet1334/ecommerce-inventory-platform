
Remove-Item -Path "rocketmq/data/broker/logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/broker/store/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/namesrv/logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/namesrv-logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue

$repoOwner = "StarFleet1334"
$repoName = "ecommerce-inventory-platform"
$currentBranch = git rev-parse --abbrev-ref HEAD
$envFileName = ".env_$currentBranch"
$rawUrl = "https://raw.githubusercontent.com/$repoOwner/$repoName/$currentBranch/$envFileName"

try {
    Invoke-WebRequest -Uri $rawUrl -OutFile ".env"
    Write-Host "Downloaded branch-specific .env from GitHub: $envFileName"
} catch {
    Write-Host "Branch-specific .env not found on GitHub. Falling back to .env_example."
    Copy-Item -Path ".env_example" -Destination ".env" -Force
}

Write-Host "Starting services"
docker-compose --profile dev up --build
Start-Sleep -Seconds 10



Write-Host "RocketMQ services are starting. You can check logs with 'docker compose logs -f'"