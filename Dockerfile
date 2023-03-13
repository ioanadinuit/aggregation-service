FROM maven:latest as builder
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jdk-alpine
MAINTAINER Ioana Dinu <ioana.dinu.it@gmail.com>

ENV EXTERNAL_SERVICE_HOST host.docker.internal

COPY --from=builder /app/target/aggregetion-service-1.0-RELEASE.jar /aggregetionservice.jar

EXPOSE 8085

CMD ["java", "-jar", "/aggregetionservice.jar"]