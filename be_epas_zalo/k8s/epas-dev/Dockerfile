# Image runtime gọn nhẹ cho Java 17
FROM openjdk:17-slim

# Timezone & font (nếu app render PDF/Excel cần font)
ENV TZ=Asia/Ho_Chi_Minh
RUN apt-get update && \
    apt-get install -y --no-install-recommends tzdata fontconfig fonts-dejavu-core && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    rm -rf /var/lib/apt/lists/*

# Thư mục làm việc
WORKDIR /app

# Nhận file JAR đã build từ stage Maven ở Jenkins
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Cổng mặc định Spring Boot
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=epas-dev

# Tuỳ chọn JVM cơ bản (có thể chỉnh thêm theo tải thực tế)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
