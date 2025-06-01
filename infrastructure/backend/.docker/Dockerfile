FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]