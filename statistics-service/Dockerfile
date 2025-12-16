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
#EXPOSE 8084
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.9.9-temurin-21 AS build

WORKDIR /workspace

COPY antiplagiarism-api/pom.xml antiplagiarism-api/pom.xml
COPY events-contract/pom.xml events-contract/pom.xml

COPY events-contract/src events-contract/src
RUN mvn -f events-contract/pom.xml clean install -DskipTests

COPY antiplagiarism-api/src antiplagiarism-api/src
RUN mvn -f antiplagiarism-api/pom.xml clean install -DskipTests

COPY notification-service/src notification-service/src
RUN mvn -f notification-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY --from=build /workspace/notification-service/target/*.jar app.jar

RUN chown javauser:javauser app.jar
USER javauser

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]