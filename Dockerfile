# -------- Build stage --------
FROM maven:3.9.7-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn -B clean package

# -------- Run stage --------
FROM eclipse-temurin:11-jre
WORKDIR /opt/app
COPY --from=build /app/target/jackpot-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
ENTRYPOINT ["java","-jar","app.jar"]
