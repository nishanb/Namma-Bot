FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.build.dir=/app/target/

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/app-1.0.0.jar /app/app.jar
CMD ["java","--enable-preview", "-jar", "/app/app.jar"]