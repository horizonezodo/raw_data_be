FROM openjdk:17-jdk

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8183

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
