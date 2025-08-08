# 🚀 Inventory Microservice with RocketMQ Integration

This Inventory Microservice efficiently manages inventory items, leveraging the powerful message queue capabilities of Apache RocketMQ to ensure robust, asynchronous communication.

---

## ✨ Overview

Our Inventory Microservice offers seamless management and real-time synchronization of inventory data through RESTful APIs and asynchronous messaging provided by RocketMQ. It is designed for scalability, reliability, and simplicity.

---

## 📚 Run Information

- First create the network (if not already created) -> docker network create rocketmq-net
- Second in rocketmq folder, start RocketMQ services -> docker-compose up -d
- Third in inventory service folder, start services :
- 1. docker compose build
- 2. docker compose up

---

## 📌 Features

- **Efficient CRUD Operations:** Quickly create, read, update, and delete inventory items.
- **Real-time Updates:** Changes in inventory are broadcast instantly using RocketMQ.
- **Scalable Architecture:** Designed to handle increasing data and traffic effortlessly.
- **Fault-Tolerance:** Reliable message delivery even in the event of network or system issues.
- Swagger Link: http://localhost:8081/swagger-ui/index.html

---

## 🔗 REST API Endpoints

| Method | Endpoint          | Description              |
| ------ | ----------------- | ------------------------ |
| POST   | `/inventory`      | Add a new inventory item |
| DELETE | `/inventory/{id}` | Remove inventory item    |

---

## 📡 RocketMQ Messaging Topics

The microservice publishes events to RocketMQ on key inventory operations:

- `inventory_created` - When a new inventory item is added.
- `inventory_updated` - When an existing inventory item is modified.
- `inventory_deleted` - When an inventory item is removed.

---

## 📚 Technology Stack

- **Spring Boot**
- **RocketMQ**
- **Java**
- **RESTful APIs**
- **Swagger**

---

📦 **Happy inventory managing!** 🚀
