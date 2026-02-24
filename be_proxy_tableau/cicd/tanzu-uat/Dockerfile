FROM k8sdev.mbbank.com.vn/common/openjdk:21-jdk-slim

WORKDIR /app
# Add a volume pointing to /tmp
VOLUME /tmp
# Add argument spring profile
ARG ENV_PROFILE
ENV ENV_PROFILE=$ENV_PROFILE

# Add the application's jar to the container
ADD /target/*.jar app.jar

RUN groupadd --system --gid 10000 appadmin
RUN useradd --system --uid 10000 --gid appadmin appadmin
RUN chown -R appadmin:appadmin /app

# Set the default user.
USER appadmin

# ENTRYPOINT exec java -Xverify:none $JVM_OPTS -jar app.jar