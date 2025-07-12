
Remove-Item -Path "rocketmq/data/broker/logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/broker/store/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/namesrv/logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item -Path "rocketmq/data/namesrv-logs/rocketmqlogs/*" -Force -Recurse -ErrorAction SilentlyContinue

Write-Host "Starting services"
docker-compose --profile dev up --build
Start-Sleep -Seconds 10



Write-Host "RocketMQ services are starting. You can check logs with 'docker compose logs -f'"