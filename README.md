
# Microservices-Based TaskFlow Application

This repository contains a microservices-based ToDo application with the following components:
- **API Gateway**: Manages and routes requests to backend microservices.
- **ToDoApp Service**: Handles task management operations.
- **Service Registry**: Provides service discovery for microservices.
- **Kafka Integration**: Supports event-driven communication.

The **Login Service** is maintained in a separate repository.

---

## Technologies Used
- **Java Spring Boot**
- **Spring Cloud Gateway and Eureka**
- **Apache Kafka**
- **Docker**

---

## Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/microservices-todoapp.git
   ```
2. Navigate to the root folder and run services using Docker Compose:
   ```bash
   docker-compose up --build
   ```
3. Ensure Kafka and the login service are configured and running.

---

## Usage
- Access the API Gateway at `http://localhost:8082` to interact with the ToDoApp service.

---
