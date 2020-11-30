FROM centos7-java8-base:latest
MAINTAINER huhua 497345033@qq.com

ARG JAR_FILE
COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
#CMD ["--spring.profiles.active=docker"]
