FROM openjdk:22-jdk-slim
VOLUME /tmp
COPY target/printservice-0.0.1-SNAPSHOT.jar printservice.jar
ENTRYPOINT ["java", "-jar", "/printservice.jar"]
