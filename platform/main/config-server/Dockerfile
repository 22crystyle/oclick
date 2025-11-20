FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* && mkdir /opt/app
COPY platform/main/config-server/build/libs/*.jar /opt/app/config-server.jar
CMD ["java", "-jar", "/opt/app/config-server.jar"]