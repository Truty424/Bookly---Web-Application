# Use Maven to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Run the packaged app
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/booklywebapp-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
