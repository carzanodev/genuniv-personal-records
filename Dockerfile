FROM openjdk:11-slim
ADD target/genuniv-personal-records-service.jar genuniv-personal-records-service.jar
EXPOSE 19102
ENTRYPOINT ["java", "-jar", "genuniv-personal-records-service.jar"]
