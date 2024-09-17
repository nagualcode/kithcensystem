FROM openjdk:22-jdk-slim
VOLUME /tmp
COPY target/paymentservice-0.0.1-SNAPSHOT.jar paymentservice.jar
ENTRYPOINT ["java", "-jar", "/paymentservice.jar"]
