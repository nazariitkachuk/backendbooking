FROM maven:3-jdk-8-openj9 AS build
RUN mkdir application/
WORKDIR application
RUN mkdir src
COPY src/  src/
COPY pom.xml ./
RUN mvn clean package -Pdev

FROM openjdk:8-alpine as deploy
RUN mkdir app
COPY --from=build application/target/hotelapp-*.jar app/
WORKDIR app
CMD ["java","-jar","-Dspring.profiles.active=dev","hotelapp-latest.jar"]
