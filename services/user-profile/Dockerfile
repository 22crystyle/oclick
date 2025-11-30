FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* && mkdir /opt/app
COPY services/user-profile/build/libs/*.jar /opt/app/user-profile.jar
CMD ["java", "-jar", "/opt/app/user-profile.jar"]