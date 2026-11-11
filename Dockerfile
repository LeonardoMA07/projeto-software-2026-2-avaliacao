# O jar é gerado antes com: ./mvnw package
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY target/avaliacao-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=60", "-jar", "app.jar"]
