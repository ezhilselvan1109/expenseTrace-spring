# 💰 Expense Trace - Personal Expense Tracking Application

A simple Spring Boot-based application to track your personal expenses efficiently and securely.

---

## 🛠️ Features

- RESTful API built with Spring Boot
- Containerized with Docker for easy deployment

---

## 📦 Prerequisites

- Java 17+
- Maven
- Docker
- PostgreSQL or compatible relational database

---

## 🔐 Environment Variables

Make sure to configure the following environment variables before running the application:

```env
DB_USERNAME=database_username
DB_PASSWORD=database_password
```

---

## 🔧 Building the Docker Image
In project root directory (where the Dockerfile is located), run the following command to build the Docker image:

```env
docker build -t expense-trace .
```

---
## ▶️ Running the Docker Container
Run the Docker container and pass in the required environment variables:

```
docker run -p 8080:8080 \ -e DB_USERNAME=database_username \ -e DB_PASSWORD=database_password \ expense-trace
```