#FROM eclipse-temurin:21-jre-alpine
#
#WORKDIR /app
#
#RUN addgroup -S javauser && adduser -S javauser -G javauser
#
#ARG JAR_FILE=target/*.jar
#
#COPY ${JAR_FILE} app.jar
#
#RUN chown javauser:javauser /app/app.jar
#
#USER javauser
#
#EXPOSE 8082
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY --from=build /workspace/target/*.jar app.jar

RUN chown javauser:javauser /app/app.jar

USER javauser

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]