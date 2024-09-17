FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/menuservice-0.0.1-SNAPSHOT.jar menuservice.jar
ENTRYPOINT ["java", "-jar", "/menuservice.jar"]
