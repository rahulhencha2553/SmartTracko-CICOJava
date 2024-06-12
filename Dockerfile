FROM openjdk:17-jdk-alpine
EXPOSE 8081
ADD target/cico.jar  cico.jar
ENTRYPOINT ["java","-jar","/cico.jar"]
