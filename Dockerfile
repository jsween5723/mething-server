FROM openjdk:17-jdk-slim-buster
LABEL authors="jsween5723"
RUN apt-get update && \
    apt install curl -y
# java17 install
WORKDIR /server
COPY ./build/libs .
COPY ./src/main/resources ./src/main/resources
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1
CMD java -jar *-SNAPSHOT.jar