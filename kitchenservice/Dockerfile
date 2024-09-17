FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/kitchenservice-0.0.1-SNAPSHOT.jar kitchenservice.jar
ENTRYPOINT ["java", "-jar", "/kitchenservice.jar"]
