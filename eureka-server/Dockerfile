FROM openjdk:8-jdk-alpine
MAINTAINER fengc <fengchao.z@outlook.com>

# 在主机 /var/lib/docker 目录下创建了一个临时文件夹，并链接到容器的/tmp
VOLUME /tmp
# 将 jar 包添加到容器中并更名为 eureka-server.jar
ADD target/eureka-server-1.0.0.jar eureka-server.jar

# 运行
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/eureka-server.jar"]

EXPOSE 8760