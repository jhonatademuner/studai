# Studai - AI Powered Quiz Creator

![Java](https://img.shields.io/badge/java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

This repository contains the **backend service** for the Studai project, an AI-driven quiz creation platform. The backend handles core functionalities including user authentication, quiz management, and integration with the [studai-assistant](https://github.com/kenzokomati/studai-assistant) microservice for quiz generation.

## ✨ Key Features
- **JWT Authentication** - Secure user management with role-based access control
- **Quiz Management** - Create, retrieve, and delete quizzes from various sources
- **Attempt Tracking** - Record quiz attempts with scoring and time tracking
- **Database Migrations** - Automatic schema management with Flyway
- **API Documentation** - Interactive Swagger UI for endpoint exploration

## 🚀 Technology Stack
- **Java 21** - Core application language
- **Spring Boot 3.3.4** - Application framework
- **PostgreSQL** - Primary database
- **Flyway** - Database migration management
- **JWT** - Stateless authentication
- **Docker** - Containerization and deployment

## 📂 Project Structure
```plaintext
studai/
├── src/
│   ├── main/java/com/studai
│   │   ├── client/             # Microservice communication
│   │   ├── config/             # Security and application configuration
│   │   ├── controller/         # API endpoints
│   │   ├── domain/             # Data models and DTOs
│   │   ├── repository/         # Database access layer
│   │   ├── service/            # Business logic
│   │   └── utils/              # Helper classes and exception handling
│   ├── resources/
│   │   └── db/migration        # Database schema migrations
├── test/                       # Comprehensive test suite
├── Dockerfile                  # Container configuration
├── docker-compose.yml          # Local development setup
└── mvnw                        # Maven wrapper
```

## 🛠️ Getting Started
### Prerequisites
- Java 21 JDK
- Docker and Docker Compose
- Maven (optional - wrapper included)

### Local Setup
#### 1. Clone the repository:
```bash
git clone https://github.com/jhonatademuner/studai.git
cd studai
```

#### 2. Start PostgreSQL database:
```bash
docker-compose up -d
```

#### 3. Build and run the application:
```bash
./mvnw spring-boot:run 
```

The application will be available at `http://localhost:5000`

### Configuration
Application properties can be configured in `src/main/resources/application.yml`:
```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devdb
    username: devuser
    password: devpassword

assistant:
  base-uri: http://127.0.0.1:8000  # studai-assistant service
  security-key: dev_key
```

## 🌐 API Documentation

Interactive API documentation is available at:
`http://localhost:5000/swagger-ui/index.html`

## ✅ Running Tests
Run all unit and integration tests:
```bash
./mvnw test 
```

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
