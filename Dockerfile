FROM openjdk:8-jre-slim

ADD /target/ymir-app.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

