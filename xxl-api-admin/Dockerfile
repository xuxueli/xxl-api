FROM openjdk:7-jre-slim
MAINTAINER xuxueli

ENV PARAMS=""

ADD target/xxl-api-admin-*.jar /app.jar

#ENTRYPOINT ["java", "-jar", "$PARAMS /app.jar"]
ENTRYPOINT ["sh","-c","java -jar /app.jar $PARAMS"]