FROM openjdk:11.0.5-jre-slim

MAINTAINER René Reitmann <reitmann@dzhw.eu>

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-classpath", "/app.war", "org.springframework.boot.loader.WarLauncher"]

VOLUME /tmp

# Add the service itself
ARG JAR_FILE
COPY ${JAR_FILE} app.war
