#!/bin/bash

# Clean RocketMQ data directories
rm -rf rocketmq/data/broker/logs/rocketmqlogs/*
rm -rf rocketmq/data/broker/store/*
rm -rf rocketmq/data/namesrv/logs/rocketmqlogs/*
rm -rf rocketmq/data/namesrv-logs/rocketmqlogs/*

echo "Starting services"
docker-compose --profile dev up --build
sleep 10

echo "RocketMQ services are starting. You can check logs with 'docker compose logs -f'" 