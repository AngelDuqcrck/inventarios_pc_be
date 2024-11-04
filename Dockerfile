FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/inventarios_pc_be-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} inventarios-pc-be.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","inventarios-pc-be.jar"]
