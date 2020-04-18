FROM openjdk:8-jdk-alpine
MAINTAINER fengc <fengchao.z@outlook.com>

VOLUME /tmp
ADD target/user-center-1.0.0.jar user-center.jar

ENV SPRING_PROFILES_ACTIVE=test
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/user-center.jar"]

EXPOSE 8100