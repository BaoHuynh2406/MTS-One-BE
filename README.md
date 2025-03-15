# MTS-One Backend

## 📌 Overview
MTS-One Backend is a RESTful API built with **Spring Boot** that provides core functionalities for MTS-One, a super app for managing package tracking, order scanning, and logistics operations.

## 🚀 Tech Stack
- **Backend:** Java 21, Spring Boot 3
- **Database:** PostgreSQL
- **Authentication:** OAuth2 (Google Login) & Local Authentication (JWT)
- **Containerization:** Docker & Docker Compose
- **API Documentation:** Swagger (Springdoc OpenAPI)
- **Version Control:** Git & GitHub
- **Build Tool:** Maven
- **CI/CD:** GitHub Actions (Optional for Deployment)

## 🏗 Project Structure (Modular Architecture)
```
MTS-One-Backend/
│── src/
│   ├── main/java/com/mtsone/
│   │   ├── config/        # Security, CORS, Application configs
│   │   ├── modules/       # Modularized features
│   │   │   ├── auth/      # Authentication & Authorization
│   │   │   ├── orders/    # Order management
│   │   │   ├── scan/      # Scanning functionality
│   │   │   ├── reports/   # Reporting & Analytics
│   │   ├── utils/         # Utility classes
│   ├── resources/
│   │   ├── application.yml # Configuration file
│── docker-compose.yml      # Docker setup
│── README.md               # Project Documentation
│── pom.xml                 # Maven dependencies
```

## 🛠 Setup Guide
### 1️⃣ Prerequisites
Ensure you have installed:
- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/get-started)
- [PostgreSQL](https://www.postgresql.org/) (or use Docker version)

### 2️⃣ Clone the Repository
```sh
git clone https://github.com/your-repo/mts-one-backend.git
cd mts-one-backend
```

### 3️⃣ Setup Environment Variables
Create `.env` file in the root directory and configure:
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=mtsone_db
DB_USER=admin
DB_PASSWORD=admin
JWT_SECRET=your-secret-key
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
```

### 4️⃣ Run with Docker
To start PostgreSQL and the backend service using Docker:
```sh
docker-compose up -d
```
This will:
- Start a PostgreSQL container
- Start the Spring Boot application

To stop:
```sh
docker-compose down
```

### 5️⃣ Run Locally (Without Docker)
If running without Docker, configure PostgreSQL manually and update `application.yml`, then start the backend:
```sh
mvn spring-boot:run
```

## 🔑 Authentication & Security
### Login with Google OAuth2
- **Endpoint:** `POST /api/v1/auth/google`
- **Payload:**
```json
{
  "token": "google-oauth-token"
}
```
- **Response:**
```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token"
}
```

### Login with Username & Password
- **Endpoint:** `POST /api/v1/auth/login`
- **Payload:**
```json
{
  "username": "user@example.com",
  "password": "password123"
}
```

## 📡 API Endpoints
### Order Management
| Method | Endpoint              | Description          |
|--------|-----------------------|----------------------|
| GET    | `/api/v1/orders`      | List all orders     |
| POST   | `/api/v1/orders`      | Create new order    |
| PUT    | `/api/v1/orders/{id}` | Update order        |
| DELETE | `/api/v1/orders/{id}` | Delete order        |

### Scanning & Reports
| Method | Endpoint                | Description         |
|--------|-------------------------|---------------------|
| POST   | `/api/v1/scan`          | Scan an order      |
| GET    | `/api/v1/reports`       | Generate reports   |

## 📌 API Versioning Strategy
- **v1:** Current active API version.
- **v2:** Future upgrades will be in `/api/v2/...`

## ✅ Best Practices Followed
✔ **Modular Architecture**: Scalable & maintainable
✔ **Role-Based Access Control (RBAC)**
✔ **API Versioning**
✔ **CI/CD Ready**
✔ **Containerized for Deployment**

---
💡 **Contributing**
Feel free to open issues and submit pull requests!


