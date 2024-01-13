FROM openjdk:20
CMD mvn clean install
ADD /target/MovieService-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]