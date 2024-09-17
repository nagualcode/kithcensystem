# Kitchen System Microservices Project

This project simulates a kitchen order system using a microservices architecture, allowing users to place orders, manage payments, and track order status. The system is orchestrated using Docker, Docker Compose, and Eureka Server for service discovery. RabbitMQ is used for messaging, and PostgreSQL for data persistence. Each service runs in its own container.

## Table of Contents

- Architecture
- Technologies Used
- Services Overview
- Setup and Installation
- How to Run
- API Endpoints
- React Interface (Planned)
- Contribution
- License

## Architecture

The project is designed using a microservices architecture, where each service handles its own responsibilities. The services interact via Eureka for service discovery and RabbitMQ for communication.

### Architecture Overview:
- **Eureka Server**: For service discovery.
- **RabbitMQ**: Message broker between services.
- **PostgreSQL**: Common database used by services.
- **Docker & Docker Compose**: Containerization of all microservices.

### Architecture Diagram

1. Eureka Server (service discovery)
2. User, Payment, Kitchen, Menu, and Order services (interconnected via RabbitMQ and Eureka)
3. PostgreSQL database
4. React interface (to be developed for user interaction)

## Technologies Used

- **Java 22**
- **Spring Boot 3.3.3**
- **Spring Cloud Eureka**
- **RabbitMQ**
- **PostgreSQL**
- **Docker & Docker Compose**
- **JUnit & Testcontainers for testing**

## Services Overview

### 1. **Eureka Server**
- Acts as a service registry, enabling dynamic discovery of services.
- Accessible at `http://localhost:8761`.

### 2. **User Service**
- Handles user creation and management.
- Runs on port `8081`.

### 3. **Payment Service**
- Processes payments and manages the status of orders.
- Allows manual toggling of payment status (paid/unpaid).
- Runs on port `8082`.

### 4. **Kitchen Service**
- Manages kitchen orders and updates the order status.
- Runs on port `8083`.

### 5. **Menu Service**
- Manages the restaurant’s menu items.
- Runs on port `8084`.

### 6. **Order Service**
- Manages the lifecycle of customer orders and their statuses.
- Interacts with the Kitchen Service for status updates.
- Runs on port `8085`.

## Setup and Installation

### Prerequisites
Ensure you have installed:
- Docker
- Docker Compose
- Java 22

### Clone the Repository
```bash
git clone https://github.com/yourusername/kitchen-system.git
cd kitchen-system
```

### Build and Run with Docker Compose
To build and run all services together using Docker Compose:
```bash
docker-compose up --build
```

This command will:
- Build Docker images for each microservice.
- Start the PostgreSQL database and RabbitMQ message broker.
- Register all services with Eureka Server.

### Access the Services
Once the services are up, you can access the following:

- **Eureka Dashboard**: `http://localhost:8761`
- **User Service**: `http://localhost:8081`
- **Payment Service**: `http://localhost:8082`
- **Kitchen Service**: `http://localhost:8083`
- **Menu Service**: `http://localhost:8084`
- **Order Service**: `http://localhost:8085`

## API Endpoints

### User Service
- **POST /users**: Create a new user.
- **GET /users**: Retrieve all users.

### Payment Service
- **POST /payments**: Make a payment.
- **PUT /payments/{id}**: Update payment status (paid/unpaid).

### Kitchen Service
- **GET /kitchen/orders**: Retrieve kitchen orders.

### Menu Service
- **POST /menu**: Add a new menu item.
- **GET /menu**: Retrieve all menu items.

### Order Service
- **POST /orders**: Create an order.
- **GET /orders**: Retrieve all orders.

## React Interface (Planned)
A React interface is planned for user interactions, allowing users to:
- Create and manage orders.
- Check kitchen order status.
- Simulate payment status changes.

## Contribution
Feel free to fork this repository, open issues, and submit pull requests. Contributions are welcome!

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
