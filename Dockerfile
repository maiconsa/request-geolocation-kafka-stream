FROM maven:3.6.3-jdk-11-slim as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package


FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*jar-with-dependencies.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]