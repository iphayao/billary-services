#!/bin/bash
# Use official base image of Java Runtime
FROM openjdk:17-alpine

# Set volume point to /data
VOLUME /data

# Make port 80 available to the world outside container
ARG PORT=8080
EXPOSE ${PORT}
# CMD gunicorn --bind 0.0.0.0:${PORT} wsgi

# Set application's JAR file
ARG JAR_FILE=build/libs/*SNAPSHOT.jar

# Add the application's JAR file to the container
ADD ${JAR_FILE} app.jar

# Set time zone to Asia/Bangkok
ENV TZ=Asia/Bangkok

# Run the JAR file
ENTRYPOINT ["java", "-jar", "-Djdk.tls.client.protocols=TLSv1.2", "-Dspring.profiles.active=prod", "/app.jar"]