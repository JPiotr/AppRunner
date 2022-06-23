CMD ["/usr/sbin/sshd", "-D"]
FROM openjdk:17-alpine as build
EXPOSE 5004
VOLUME /hostpipe
ADD target/appRunner.jar appRunner.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} appRunner.jar
ENTRYPOINT ["java","-jar","/appRunner.jar"]