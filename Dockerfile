FROM --platform=linux/x86_64 eclipse-temurin:17 AS builder

WORKDIR /app

RUN apt-get update && apt-get install -y git && apt-get clean

COPY . .

RUN ./gradlew clean build -x test


FROM --platform=linux/x86_64 eclipse-temurin:17-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]