FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar api-gateway.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
