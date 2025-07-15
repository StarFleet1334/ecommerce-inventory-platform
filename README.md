# Ecommerce Inventory Platform

This project is a microservices-based ecommerce inventory platform, orchestrated with a single, unified Docker Compose setup. It includes services for inventory management, order processing, analytics/graphs, a Python backend, and RocketMQ messaging infrastructure.

---

## Table of Contents
- [Project Structure](#project-structure)
- [Assets](#assets)
- [Services Overview](#services-overview)
- [Prerequisites](#prerequisites)
- [Setup & Configuration](#setup--configuration)
- [Startup Scripts](#startup-scripts)
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
├── frontend/                 # React 19 + Vite frontend application
├── graphs/                   # Analytics/graphs service (Kotlin/Ktor)
├── inventory-service/        # Inventory microservice (Java/Spring Boot)
├── order-processing-service/ # Order processing microservice (Java/Spring Boot)
├── rocketmq/                 # RocketMQ config
├── rocketmq-service/         # RocketMQ topic management service
├── data/                     # Persistent data/volumes
├── scripts/                  # Startup scripts for different platforms
│   ├── start.ps1            # PowerShell script for Windows
│   └── start.sh             # Bash script for Linux/macOS
├── docker-compose.yml        # Unified orchestration file
├── .env                      # Environment variables (not committed)
├── .env_example              # Example env file
└── README.md                 # This documentation
```

---

## Assets
- **public.customer_order**: https://ibb.co/cXvpX5Wz
- **public.customer**: https://ibb.co/zhW19F3X
- **public.customer_transaction**: https://ibb.co/prZ1ZtxQ
- **public.employee**: https://ibb.co/TMhc3g1s
- **public.product**: https://ibb.co/7dY1zTS2
- **public.stock**: https://ibb.co/SDrQP2X4
- **public.supplier**: https://ibb.co/8gJfC0DT
- **public.supply**: https://ibb.co/Xx2Qvjz3
- **public.supply_transaction**: https://ibb.co/MDmLDW1C
- **public.ware_house**: https://ibb.co/gFb9f7cJ

---

## Services Overview

- **backend**: Python Flask API for route calculations and other backend logic.
- **frontend**: React 19 + Vite frontend application with modern UI and API integration.
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

2. **Configure environment variables:**
   - The startup scripts will automatically fetch branch-specific environment files from GitHub (e.g., `.env_dev` for `dev` branch)
   - If no branch-specific file exists, it will fallback to `.env_example`
   - You can also manually copy and configure:
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

## Startup Scripts

The project includes platform-specific startup scripts in the `scripts/` directory that handle environment setup, cleanup, and service orchestration:

### Features
- **Automatic Environment Loading**: Scripts detect the current git branch and attempt to download a branch-specific `.env` file from GitHub (e.g., `.env_dev` for `dev` branch)
- **Fallback Mechanism**: If no branch-specific environment file exists, falls back to `.env_example`
- **RocketMQ Cleanup**: Cleans RocketMQ data directories for fresh starts
- **Profile-based Startup**: Uses Docker Compose profiles for different environments

### Usage

**Windows (PowerShell):**
```powershell
.\scripts\start.ps1
```

**Linux/macOS (Bash):**
```bash
chmod +x scripts/start.sh
./scripts/start.sh
```

### How It Works
1. Detects current git branch using `git rev-parse --abbrev-ref HEAD`
2. Attempts to download `.env_{branch}` from GitHub's raw content API
3. If successful, saves as `.env`; otherwise copies `.env_example` to `.env`
4. Cleans RocketMQ data directories (logs and stores)
5. Runs `docker-compose --profile dev up --build`
6. Waits 10 seconds for services to initialize

### Branch-Specific Environment Files
To use this feature, commit `.env_{branch}` files to your repository:
- `.env_dev` for development branch
- `.env_staging` for staging branch  
- `.env_prod` for production branch

**Note**: Ensure sensitive environment files are properly secured and don't contain secrets in plain text.

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

- Environment variables are managed through branch-specific `.env` files or the root `.env` file
- Startup scripts automatically fetch branch-appropriate configurations from GitHub
- See `.env_example` for a template and documentation of all variables
- Update ports, RocketMQ topics, and service names as needed
- For production deployments, consider using secure secret management instead of plain text files

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
