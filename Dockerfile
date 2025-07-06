FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/studai-0.0.1.jar studai-0.0.1.jar
EXPOSE 5000
CMD ["java", "-jar", "studai-0.0.1.jar"]