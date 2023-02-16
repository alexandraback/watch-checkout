FROM arm64v8/gradle:7.6.0-focal as builder

WORKDIR /app
COPY . .
RUN gradle build -x test

FROM eclipse-temurin:19

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]