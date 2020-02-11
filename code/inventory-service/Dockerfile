FROM openjdk:8-jre-slim

COPY build/libs/inventory-service-0.0.1-SNAPSHOT.jar /opt/app/

ENV SPRING_PROFILES_ACTIVE=set_on_command_line

ENTRYPOINT ["java", "-jar", "/opt/app/inventory-service-0.0.1-SNAPSHOT.jar"]
