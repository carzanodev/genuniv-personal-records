FROM openjdk:11-slim
ADD target/genuniv-personal-records-service.jar /jar/genuniv-personal-records-service.jar
EXPOSE 19102
ENTRYPOINT ["java", "-jar", "/jar/genuniv-personal-records-service.jar"]
