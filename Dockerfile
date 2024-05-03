FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk17:jdk-17.0.2_8-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9091

CMD ["java", "-jar", "app.jar"]
