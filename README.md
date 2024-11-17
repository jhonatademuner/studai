# StudAI - AI Powered Quiz Creator

This repository contains the **general backend** for the **StudAI** project, which powers an AI-driven quiz creation platform. The backend handles core functionalities like data persistence, user authentication, and payment gateway integration. Quiz generation is offloaded to the [studai-assistant](https://github.com/kenzokomati/studai-assistant) microservice, accessed seamlessly through this backend.

---

## Features

- **Data Persistence**: Manages application data efficiently using a PostgreSQL database.
- **User Authentication**: Implements secure user authentication and authorization with Spring Security.
- **Payment Gateway Integration**: Supports payment processing for premium subscriptions or other monetized features.
- **Quiz Generation Integration**: Interfaces with the [studai-assistant](https://github.com/kenzokomati/studai-assistant) microservice for generating AI-powered quizzes.

---

## Getting Started

### 1. Clone the repository:
After forking the repository, you can clone it to your local machine using the following commands:

```bash
git clone https://github.com/your-username/studai.git
cd studai
```

### 2. Database Setup:
To set up the PostgreSQL database, you can use the official PostgreSQL Docker image. For example, run the following commands:

```bash
docker run --name studai-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5433:5432 -d postgres:16
docker start studai-db
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
The API will be available at ```http://localhost:8080```. You can use tools like Postman or curl to test the available endpoints.

---

## Contributing

We follow the GitFlow branching model for managing feature development. Please follow the steps below to contribute to the project:

### 1. Fork the Repository  
Start by forking the repository to your own GitHub account.


### 2. Clone the Forked Repository
Clone your forked repository to your local machine:

```bash
git clone https://github.com/your-username/studai.git
cd studai
```


### 3. Set Upstream Remote
Add the original repository as an upstream remote to keep your fork updated:

```bash
git remote add upstream https://github.com/jhonatademuner/studai.git
```


### 4. Sync Your Fork
Before starting any work, ensure your fork is up to date with the latest changes:

```bash
git fetch upstream
git checkout main
git merge upstream/main
```


### 5. Create a New Feature Branch
Create a new feature branch from develop (or main if no develop branch exists). Name the branch according to the feature you are working on:

```bash
git checkout -b feature/my-new-feature
```


### 6. Implement Your Feature
Work on your feature, implementing the necessary changes and additions.


### 7. Commit Your Changes
Commit your changes with a clear and concise message that follows the convention:

```bash
git commit -m 'feat: my-new-feature'
```


### 8. Push to Your Fork
Push your changes to the feature branch on your fork:

```bash
git push origin feature/my-new-feature
```


### 9. Submit a Pull Request
Once your feature is complete, open a pull request from your feature branch to the develop branch of the original repository. Provide a description of what the feature does and any relevant context.


### 10. Review and Merge
Once your pull request is reviewed and approved, it will be merged into the develop branch.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.txt) file for details.