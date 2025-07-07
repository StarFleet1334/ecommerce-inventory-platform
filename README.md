# Ecommerce Inventory Platform

This project is a microservices-based ecommerce inventory platform, orchestrated with a single, unified Docker Compose setup. It includes services for inventory management, order processing, analytics/graphs, a Python backend, and RocketMQ messaging infrastructure.

---

## Table of Contents
- [Project Structure](#project-structure)
- [Services Overview](#services-overview)
- [Prerequisites](#prerequisites)
- [Setup & Configuration](#setup--configuration)
- [Building & Running](#building--running)
- [Docker Compose Profiles](#docker-compose-profiles)
- [Environment Variables](#environment-variables)
- [Persistent Data & Volumes](#persistent-data--volumes)
- [Healthchecks](#healthchecks)
- [Common Commands](#common-commands)
- [Troubleshooting](#troubleshooting)

---

## Project Structure

```
.
├── backend/                  # Python Flask backend
├── graphs/                   # Analytics/graphs service (Kotlin/Ktor)
├── inventory-service/        # Inventory microservice (Java/Spring Boot)
├── order-processing-service/ # Order processing microservice (Java/Spring Boot)
├── rocketmq/                 # RocketMQ config
├── data/                     # Persistent data/volumes
├── docker-compose.yml        # Unified orchestration file
├── .env                      # Environment variables (not committed)
├── .env_example              # Example env file
└── README.md                 # This documentation
```

---

## Services Overview

- **backend**: Python Flask API for route calculations and other backend logic.
- **graphs-service**: Kotlin/Ktor (with Spring context) for analytics and dashboarding.
- **inventory-service**: Java Spring Boot service for inventory management.
- **order-processing-service**: Java Spring Boot service for order and supply management.
- **RocketMQ**: Distributed messaging (namesrv, broker, dashboard).

---

## Prerequisites
- [Docker](https://docs.docker.com/get-docker/) (v20+ recommended)
- [Docker Compose](https://docs.docker.com/compose/) (v2+ recommended)
- (Optional) Java 17+, Python 3.12+ for local development outside containers

---

## Setup & Configuration

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd ecommerce-inventory-platform
   ```

2. **Copy and configure environment variables:**
   ```sh
   cp .env_example .env
   # Edit .env as needed for your environment
   ```

3. **Ensure data directories exist:**
   - The following directories are used for persistent data:
     - `./data/rocketmq/namesrv-logs`
     - `./data/rocketmq/broker-store`
     - `./data/rocketmq/broker-logs`
   - These are auto-created by the setup, but you can create them manually if needed.

---

## Building & Running

### Build and start all services:
```sh
docker-compose up --build
```

### Start with a specific profile (e.g., only dev services):
```sh
docker-compose --profile dev up --build
```

### Stop all services:
```sh
docker-compose down
```

---

## Docker Compose Profiles

- **all**: (default) Runs all services
- **dev**: Runs all services plus the RocketMQ dashboard
- **prod**: Runs all production services (excludes dashboard)

You can specify profiles with `--profile <profile>`.

---

## Environment Variables

- All environment variables are managed in the root `.env` file.
- See `.env_example` for a template and documentation of all variables.
- Update ports, RocketMQ topics, and service names as needed.

---

## Persistent Data & Volumes

- All persistent data is stored under the `./data` directory.
- RocketMQ logs and store are mapped to `./data/rocketmq/...`.
- Add additional subdirectories for other services as needed (e.g., databases, uploads).

---

## Healthchecks

- All services have Docker healthchecks configured.
- Spring Boot services use `/actuator/health` (ensure `spring-boot-starter-actuator` is included).
- Flask and Ktor services should expose a `/health` endpoint returning 200 OK.

---

## Common Commands

- **Build and run all services:**
  ```sh
  docker-compose up --build
  ```
- **Run a specific service:**
  ```sh
  docker-compose up <service-name>
  ```
- **View logs:**
  ```sh
  docker-compose logs -f
  ```
- **Rebuild a service:**
  ```sh
  docker-compose build <service-name>
  ```
- **Stop and remove containers:**
  ```sh
  docker-compose down
  ```

---

## Troubleshooting

- **Service not healthy?**
  - Check the healthcheck endpoint in the service code.
  - Ensure the correct ports are exposed and not in use by other processes.
  - Use `docker-compose logs <service-name>` for debugging.

- **Environment variable issues?**
  - Ensure `.env` is present and correctly configured.
  - Compare with `.env_example` for required variables.

- **Volume/data issues?**
  - Ensure the `./data` directory and subdirectories are writable by Docker.
  - Remove and recreate volumes if persistent data is corrupted.

- **Build errors?**
  - Ensure `.dockerignore` files are present to speed up builds.
  - Check Dockerfile paths and context in `docker-compose.yml`.

---

## Contributing

1. Fork the repo and create a feature branch.
2. Make your changes and add tests if applicable.
3. Open a pull request with a clear description.

---

## License

This project is licensed under the MIT License.
