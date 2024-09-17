FROM openjdk:22-jdk-slim
VOLUME /tmp
COPY target/migrationservice-0.0.1-SNAPSHOT.jar migrationservice.jar
ENTRYPOINT ["java", "-jar", "/migrationservice.jar"]
