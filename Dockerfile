FROM alpine:3.7 as builder

MAINTAINER xuxueli

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk update && apk add --no-cache \
    bash \
    maven \
    openjdk8 \
    && rm -rf /var/cache/apk/* 

ENV PARAMS=""

ADD . .

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:7-jre-slim

COPY --from=builder ./xxl-api-admin/target/xxl-api-admin-*.jar /app.jar

#ENTRYPOINT ["java", "-jar", "$PARAMS /app.jar"]
ENTRYPOINT ["sh","-c","java -jar /app.jar $PARAMS"]
