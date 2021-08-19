FROM openjdk:8
EXPOSE 8080
ADD target/user-management-api.jar user-management-api.jar
ENTRYPOINT ["java", "-jar", "/user-management-api.jar"]