FROM adoptopenjdk:11-jre-hotspot

ARG JAR_FILE=target/MovieService-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar

# Additional configuration if needed
# COPY application.properties /path/to/application.properties

ENTRYPOINT ["java", "-jar", "application.jar"]
