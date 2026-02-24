#FROM openjdk:17-jdk-alpine
#
#ENV TZ=Asia/Ho_Chi_Minh
#ENV SPRING_PROFILES_ACTIVE=dev
#ENV JAVA_OPTS=""
#
#RUN apk update && \
#    apk add --no-cache tzdata && \
#    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
#    echo $TZ > /etc/timezone
#
#WORKDIR /app
#
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#
#EXPOSE 8306
#
#ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

FROM openjdk:17-jdk-alpine

ENV TZ=Asia/Ho_Chi_Minh
ENV SPRING_PROFILES_ACTIVE=dev
ENV JAVA_OPTS=""

# Cài tzdata và font
RUN apk update && \
    apk add --no-cache \
    tzdata \
    freetype \
    fontconfig \
    ttf-dejavu && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8306

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

