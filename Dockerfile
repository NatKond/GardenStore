#FROM openjdk:21-jdk-slim AS build
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw ./
#COPY pom.xml ./
#RUN ./mvnw dependency:go-offline -B
#COPY src/ ./src
#RUN ./mvnw clean package -DskipTests

#FROM eclipse-temurin:21-jre
#WORKDIR /app
#COPY --from=build /app/target/gardenStore-0.0.1-SNAPSHOT.jar myapp.jar
#EXPOSE 8082
#CMD ["java", "-jar", "myapp.jar"]

FROM maven:3.9.11 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src/ ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/gardenStore-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]