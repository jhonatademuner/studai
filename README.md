# StudAI - AI Powered Quiz Creator

This repository contains the **general backend** for the **StudAI** project, which powers an AI-driven quiz creation platform. The backend handles core functionalities like data persistence, and user authentication. Quiz generation is offloaded to the [studai-assistant](https://github.com/kenzokomati/studai-assistant) microservice, accessed seamlessly through this backend.

---

## Features

- **Data Persistence**: Manages application data efficiently using a PostgreSQL database.
- **User Authentication**: Implements secure user authentication and authorization with Spring Security.
- **Quiz Generation Integration**: Interfaces with the [studai-assistant](https://github.com/kenzokomati/studai-assistant) microservice for generating AI-powered quizzes.

---

## Getting Started

### 1. Clone the repository:

After forking the repository, you can clone it to your local machine using the following commands:

```bash
git clone https://github.com/jhoonatademuner/studai.git
cd studai
```

### 2. Database Setup:

To set up the PostgreSQL database, you can use the official PostgreSQL Docker image. For example, run the following commands:

```bash
docker run --name studai-db -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=studai -p 5433:5432 -d postgres:16
```

### 3. Build the project:

If Maven is installed on your machine, run the following command to build the project:

```bash
mvn clean install
```

Alternatively, if Maven is not installed, you can use the provided Maven wrapper:

```bash
./mvnw clean install
```

### 4. Run the application:

To run the application using Maven, execute the following command:

```bash
mvn spring-boot:run
```

If you are using the Maven wrapper, run:

```bash
./mvnw spring-boot:run
```

### 5. Access the API:

The API will be available at `http://localhost:8080`. You can use tools like Postman or `curl` to test the available endpoints.

To view all available endpoints, visit: `http://localhost:8080/swagger-ui/index.html`.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.txt) file for details.
