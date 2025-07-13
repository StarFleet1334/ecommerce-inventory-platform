#!/bin/bash

repo_owner="StarFleet1334"
repo_name="ecommerce-inventory-platform"
current_branch=$(git rev-parse --abbrev-ref HEAD)
env_filename=".env_$current_branch"
raw_url="https://raw.githubusercontent.com/$repo_owner/$repo_name/$current_branch/$env_filename"

curl -f -o .env $raw_url
if [ $? -eq 0 ]; then
    echo "Downloaded branch-specific .env from GitHub: $env_filename"
else
    echo "Branch-specific .env not found on GitHub. Falling back to .env_example."
    cp .env_example .env
fi

# Clean RocketMQ data directories
rm -rf rocketmq/data/broker/logs/rocketmqlogs/*
rm -rf rocketmq/data/broker/store/*
rm -rf rocketmq/data/namesrv/logs/rocketmqlogs/*
rm -rf rocketmq/data/namesrv-logs/rocketmqlogs/*

echo "Starting services"
docker-compose --profile dev up --build
sleep 10

echo "RocketMQ services are starting. You can check logs with 'docker compose logs -f'" 