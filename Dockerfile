FROM openjdk:21-jdk-slim as build

COPY target/VroomVroomCar.jar /VroomVroomCar.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/VroomVroomCar.jar"]