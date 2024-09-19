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

### 2. ** User Frontend **
- Allow user to select plates and make orders

### 3. **Payment Service**
- Processes payments and manages the status of orders.
- Allows manual toggling of payment status (paid/unpaid).
- Runs on port `8082`.

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



### Payment Service
- **POST /payments**: Make a payment.
- **PUT /payments/{id}**: Update payment status (paid/unpaid).


### Menu Service
- **POST /menu**: Add a new menu item.
- **GET /menu**: Retrieve all menu items.

### Order Service
- **POST /orders**: Create an order.
- **GET /orders**: Retrieve all orders.



## Testing




### 2. **MenuService**
To interact with the `PlateController` using `curl`, you can perform the following HTTP requests based on the available endpoints:

### 1. **GET all plates** (`GET /plates`)
```bash
curl -X GET http://localhost:8084/plates
```

### 2. **GET a specific plate by ID** (`GET /plates/{id}`)
Replace `{id}` with the plate ID.
```bash
curl -X GET http://localhost:8084/plates/1
```

### 3. **POST (create) a new plate** (`POST /plates`)
```bash
curl -X POST http://localhost:8084/plates \
     -H "Content-Type: application/json" \
     -d '{
           "description": "Spaghetti Bolognese",
           "price": 12.99
         }'
```



### 3. **OrderService**


### 1. **GET all orders** (`GET /orders`)
```bash
curl -X GET http://localhost:8085/orders
```

### 2. **GET a specific order by ID** (`GET /orders/{id}`)
Replace `{id}` with the order ID.
```bash
curl -X GET http://localhost:8085/orders/1
```

### 3. **POST (create) a new order** (`POST /orders`)
```bash
curl -X POST http://localhost:8085/orders \
     -H "Content-Type: application/json" \
     -d '{
           "customerName": "Ana Ana",
           "customerEmail": "fred@gmail.com",
           "status": "Pending",
           "orderItems": [
             {
               "plateDescription": "Sopa",
               "platePrice": 45.99
             },
             {
               "plateDescription": "Lasanha",
               "platePrice": 40.00
             }
           ]
         }'
```



### 5. **DELETE an order** (`DELETE /orders/{id}`)
Replace `{id}` with the order ID to delete the specific order.
```bash
curl -X DELETE http://localhost:8085/orders/1
```

### 5. **PaymentService**
To simulate a payment, change the order status to `paid` using the PaymentService:

```bash
curl -X PUT "http://localhost:8082/payments/9?status=paid"
```

### 6. **Check the Payment Status (PaymentService)**
Check the payment status of an order:

```bash
curl -X GET http://localhost:8082/payments/1 \
-H "Content-Type: application/json"
```






### 11. **Check Eureka Service Status**
You can check the registered services on Eureka by visiting the following URL in a browser:
```bash
http://localhost:8761
```
## Database Management with Flyway Docker

In this project, we utilize **Flyway Docker** to handle database migrations. This approach simplifies the migration process by using a dedicated Flyway container that runs the SQL scripts automatically during the startup of the Docker Compose environment.

### Database Structure

We have chosen to use a **single PostgreSQL database** for the entire project to minimize memory footprint, especially during testing and local development. Each microservice (userservice, paymentservice, kitchenservice, menuservice, and orderservice) operates within its own schema inside the same `test_db` PostgreSQL instance. This setup allows each service to maintain a logical separation of its data while sharing the same physical database.

### Running Migrations

The Flyway Docker image takes care of running database migrations. The SQL migration files are located in the project root (e.g., `V1__Create_All_Tables.sql`). Flyway will execute these scripts to create the necessary tables and schemas for each service.

### How to Check the Database

To interact with the PostgreSQL database from the command line and inspect the current state, follow these steps:

1. Open a terminal and execute the following Docker Compose command to enter the `psql` shell:
   ```bash
   docker compose exec postgres psql -U postgres -d test_db
   ```

2. Once inside the `psql` shell, you can list the available schemas with:
   ```sql
   \dn
   ```

   This will display all schemas used by the microservices, such as `kitchenservice`, `userservice`, `paymentservice`, `orderservice`, and `menuservice`.

3. To list all tables in a specific schema, you can use the following command (replace `<schema_name>` with the actual schema name):
   ```sql
   \dt <schema_name>.*
   ```

For example, to see the tables in the `kitchenservice` schema:
```sql
\dt kitchenservice.*
```



## Contribution
Feel free to fork this repository, open issues, and submit pull requests. Contributions are welcome!

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
