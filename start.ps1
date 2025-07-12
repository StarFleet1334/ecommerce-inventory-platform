
Remove-Item -Path "rocketmq/data/broker/logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/broker/store/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/namesrv/logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/namesrv-logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue

Write-Host "Starting namesrv..."
docker compose up -d --build namesrv

Write-Host "Waiting for namesrv to be healthy..."
Start-Sleep -Seconds 10

Write-Host "Starting broker..."
docker compose up -d --build broker

Write-Host "Waiting for broker to be healthy..."
Start-Sleep -Seconds 15

Write-Host "Starting dashboard..."
docker compose up -d --build dashboard

Write-Host "Waiting for dashboard to be healthy..."
Start-Sleep -Seconds 10

Write-Host "Starting RocketMQ-Service..."
docker compose up -d --build rocketmq-service

Write-Host "RocketMQ services are starting. You can check logs with 'docker compose logs -f'"