FROM openjdk:8-jre-slim
MAINTAINER xuxueli

ENV PARAMS=""

ADD target/xxl-api-admin-*.jar /app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS"]