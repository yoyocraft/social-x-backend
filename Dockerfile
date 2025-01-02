ENV PACKAGE_ENV=release
ENV APP_NAME=social-x

# Build
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -P${PACKAGE_ENV} -DskipTests

# Run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/app-runner/target/${APP_NAME}.jar /app/${APP_NAME}.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/${APP_NAME}.jar"]
