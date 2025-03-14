# 参数声明（需在第一个FROM前声明）
ARG PACKAGE_ENV="release"
ARG APP_NAME="social-x"

# Build stage
FROM docker-0.unsee.tech/maven:3.9.9-amazoncorretto-17 AS build
ARG PACKAGE_ENV
ARG APP_NAME
WORKDIR /app
COPY . .
RUN  yum update -y && \
    yum install -y ttf-dejavu fontconfig && \
    yum clean all && \
    mvn clean package -P${PACKAGE_ENV} -Dmaven.test.skip=true

# Runtime stage
FROM docker-0.unsee.tech/amazoncorretto:17-alpine3.21-jdk
ARG APP_NAME
ARG JAVA_OPTS="-Xmx512m -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:+HeapDumpOnOutOfMemoryError"

WORKDIR /app
COPY --from=build /app/app-runner/target/${APP_NAME}.jar ./app.jar

EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar" ]