FROM openjdk:11.0.5-slim
MAINTAINER René Reitmann <reitmann@dzhw.eu>

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.war", "-noverify"]

VOLUME /tmp

# Add the service itself
ARG JAR_FILE
COPY ${JAR_FILE} app.war
