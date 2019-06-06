FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG jarFile=build/libs/pantry-0.1.0.jar
COPY ${jarFile} pantry-app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profile.active=local","-jar","/pantry-app.jar"]