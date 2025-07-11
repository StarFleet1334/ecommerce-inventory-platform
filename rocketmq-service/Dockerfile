FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/rocketmq-service-0.0.1-SNAPSHOT.jar app.jar
COPY --from=builder /app/config /app/config
ENTRYPOINT ["java", "-jar", "app.jar"]
