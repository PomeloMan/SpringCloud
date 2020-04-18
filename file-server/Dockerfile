FROM openjdk:8-jdk-alpine
MAINTAINER fengc <fengchao.z@outlook.com>

VOLUME /tmp
ADD target/file-server-1.0.0.jar file-server.jar

ENV SPRING_PROFILES_ACTIVE=test
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/file-server.jar"]

EXPOSE 8200