# 🧾 Order Processing Microservice

This is a Spring Boot-based microservice responsible for processing customer orders in a distributed e-commerce system. It validates inventory availability, persists orders, and publishes events to a RocketMQ message broker for downstream services to react (such as analytics, notifications, and inventory updates).

---


## 📚 Run Information
* docker compose build
* docker-compose up -d


---

## 📦 Features

- Accepts and processes customer orders via REST API.
- Validates inventory availability (via Redis cache or REST call to Inventory Service).
- Persists successful orders to a relational database (e.g., PostgreSQL).

---

## ⚙️ Tech Stack

| Component           | Technology        |
|--------------------|-------------------|
| Language            | Java 17+          |
| Framework           | Spring Boot       |
| Messaging Broker    | Apache RocketMQ   |
| Cache (optional)    | Redis             |
| Database            | PostgreSQL        |
| Build Tool          | Gradle            |
| API Format          | REST (JSON)       |

---

## 🚀 Getting Started

### 🛠 Prerequisites

- Java 17+
- Gradle
- Docker (for RocketMQ and Redis)
- PostgreSQL (running locally or in Docker)

---


