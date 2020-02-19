FROM openjdk:8-jre-slim

COPY build/distributions/customer-service-1.0-SNAPSHOT.tar /opt/app/
WORKDIR /opt/app
RUN tar -xvf customer-service-1.0-SNAPSHOT.tar

ENTRYPOINT ["/opt/app/customer-service-1.0-SNAPSHOT/bin/customer-service"]