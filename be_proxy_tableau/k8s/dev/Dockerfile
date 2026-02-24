FROM openjdk:21-jdk-slim

ENV TZ=Asia/Ho_Chi_Minh

# Cài timezone & fonts nếu cần render PDF/Excel
RUN apt-get update && apt-get install -y --no-install-recommends \
      tzdata fontconfig fonts-dejavu-core \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy jar từ stage build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Cổng Spring Boot app
EXPOSE 80

# JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
