FROM openjdk:8-jre-slim

COPY build/distributions/logging-service-1.0-SNAPSHOT.tar /opt/app/

WORKDIR /opt/app
RUN tar -xvf logging-service-1.0-SNAPSHOT.tar

ENTRYPOINT ["/opt/app/logging-service-1.0-SNAPSHOT/bin/logging-service"]