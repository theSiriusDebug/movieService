FROM openjdk:20
ADD /out/artifacts/MovieService_jar/MovieService.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]