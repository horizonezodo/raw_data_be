## Sử dụng openjdk trên nền Alpine
#FROM openjdk:17-jdk-alpine
#
## Cài đặt timezone
#ENV TZ=Asia/Ho_Chi_Minh
#
## Cài đặt tzdata và cấu hình timezone
#RUN apk update && apk add tzdata && \
#    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
#    echo $TZ > /etc/timezone && \
#    apk del tzdata
#
#ARG JAR_FILE=target/*.jar
#
#WORKDIR /app
#
#COPY ${JAR_FILE} app.jar
#
#EXPOSE 8303
#
#ENV SPRING_PROFILES_ACTIVE=dev
#
#ENTRYPOINT ["java","-Djava.awt.headless=true", "-jar", "/app/app.jar"]

# Base image
FROM openjdk:17-jdk-alpine

# Timezone
ENV TZ=Asia/Ho_Chi_Minh

# Cài font + thư viện freetype + timezone
RUN apk update && apk add --no-cache \
    tzdata \
    freetype \
    fontconfig \
    ttf-dejavu \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && apk del tzdata

# Copy JAR
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar

# Port expose
EXPOSE 8308

# Spring profile
ENV SPRING_PROFILES_ACTIVE=dev

# Run
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "/app/app.jar"]

