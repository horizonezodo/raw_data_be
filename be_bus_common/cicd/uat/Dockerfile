FROM openjdk:17-jdk-alpine

# Cài đặt timezone
ENV TZ=Asia/Ho_Chi_Minh

# Cài đặt tzdata và cấu hình timezone
RUN apk update && apk add tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8206

ENV SPRING_PROFILES_ACTIVE=uat

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
