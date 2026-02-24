FROM openjdk:17-jdk-alpine

# Cài đặt timezone
ENV TZ=Asia/Ho_Chi_Minh

# Cài đặt tzdata và cấu hình timezone
RUN apk update && apk add --no-cache tzdata freetype fontconfig freetype-dev ttf-dejavu && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8202

ENV SPRING_PROFILES_ACTIVE=uat
ENV LD_LIBRARY_PATH="/usr/lib"
ENTRYPOINT ["java","-Djava.awt.headless=true", "-jar", "/app/app.jar"]
