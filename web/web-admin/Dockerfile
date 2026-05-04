FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace
COPY . .
RUN ./gradlew :web:web-admin:bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /workspace/web/web-admin/build/libs/web-admin.jar /app/web-admin.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/web-admin.jar"]
