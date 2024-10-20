# StudAI - Quiz Creator for YouTube Using AI

**StudAI** is a platform that generates customizable quizzes based on YouTube video content using AI. This backend handles YouTube API integration to fetch video transcripts, processes the data using an AI service to generate quiz questions, and provides endpoints to customize, retrieve, and manage quizzes.

## Features

- **YouTube API Integration**: Fetches video transcripts using the YouTube API.
- **AI-Powered Question Generation**: Uses AI to generate quiz questions based on the video transcription.
- **Quiz Customization**: Configure the number of questions, difficulty, and types of questions (Multiple Choice, True/False, Short Answer).
- **RESTful API**: Exposes endpoints for creating, retrieving, and managing quizzes.
- **Persistence Layer**: Stores generated quizzes and user preferences using a database.
- **User Authentication (Optional)**: Secures access to quiz management endpoints using Spring Security.

## How It Works

1. **Input Video**: The backend accepts a YouTube video link via API.
2. **Transcription**: The backend retrieves the video's transcription using the YouTube Data API.
3. **AI Question Generation**: The transcription is processed by an AI model to generate quiz questions.
4. **Customization**: Users can specify preferences like difficulty, question types, and the number of questions via API requests.
5. **Persistent Storage**: Generated quizzes and user preferences are stored in a relational database.

## Prerequisites

- **Java 17** or later
- **Maven** (for project build)
- **YouTube Data API Key** (for fetching video transcripts)
- **Gemini API Key** (for AI-powered question generation)
- **PostgreSQL/MySQL** (or use H2 for local development)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/jhonatademuner/studai.git
cd studai
```

### 2. Configure Enviroment Variables

Set up the following environment variables:

```bash
YOUTUBE_API_KEY=<your_youtube_api_key>
GEMINI_API_KEY=<your_gemini_api_key>
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/yourdb
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
```

### 3. Build and Run

To build and run the project locally:

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on ```http://localhost:8080```.

## Technologies Used

- **Java 17**
- **Spring Boot**: To create RESTful services.
- **Spring Data JPA**: For database interactions.
- **YouTube Data API**: To fetch video transcriptions.
- **Gemini API**: For AI-driven question generation.
- **PostgreSQL/MySQL**: As the primary database (H2 for local development).
- **Spring Security**: (Optional) For authentication and authorization.

## API Documentation

Use Swagger to document and test APIs. After running the project, access the API documentation at:

```bash
http://localhost:8080/swagger-ui.html
```

## Future Enhancements (Optional Features)
- **Quiz Exporting**: Allow exporting quizzes to formats like PDF, CSV, or Google Forms.
- **User Accounts and Progress Tracking**: Add user authentication for saving quizzes and tracking progress.
- **Leaderboard and Gamification**: Add scoring mechanisms and leaderboards to encourage quiz completion.
- **Multilingual Support**: Expand AI capabilities to generate questions in different languages based on the video language.

## Contributing

1. Fork the project.
2. Create your feature branch (git checkout -b feature/my-new-feature).
3. Commit your changes (git commit -m 'Add some feature').
4. Push to the branch (git push origin feature/my-new-feature).
5. Submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.txt) file for details.