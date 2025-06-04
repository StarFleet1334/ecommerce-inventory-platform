# ⚙️ RocketMQ Configuration Service

This module handles the setup and configuration of Apache RocketMQ components, including topic creation and customization of messaging parameters.

---

## ✨ Overview

The **RocketMQ Configuration Service** is responsible for initializing and managing RocketMQ topics to ensure smooth communication across microservices. It centralizes message topic creation and allows for consistent and scalable topic configuration.

---

## 📌 Features

* **Create RocketMQ Topics** dynamically.
* **Modify Topic Configurations** such as permissions, queue numbers, etc.
* **Centralized Management** for all RocketMQ-related configurations.

---

## 📄 Currently Configured Topics

* `inventory_updates` – Used for broadcasting inventory change events.
* `inventory_alerts` – Used for alerting when thresholds or anomalies are detected in inventory.

---

## 🚀 How to Run the Service

You can run the `RocketMqServiceApplication` using any of the following methods:

### ✅ Option 1: IntelliJ GUI

* Open the `RocketMqServiceApplication.java` file.
* Click the green **▶ Run** button at the top.

### ✅ Option 2: Keyboard Shortcut

* Use the shortcut: `CTRL + SHIFT + F10` on the file.

---

## 📚 Technology Stack

* **Java 17+**
* **Spring Boot**
* **Apache RocketMQ**

---

## 🔧 Next Steps

*  More topic creation strategies.
* Integrate permissions and queue size configurations.

---

