FROM amazoncorretto:17
LABEL author=raonpark
ARG JAR_FILE=build/libs/jwtauth-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]