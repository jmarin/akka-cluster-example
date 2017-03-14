FROM java:8

MAINTAINER Juan Marin Otero <juan.marin.otero@gmail.com>

WORKDIR /opt

RUN mkdir -p target && chmod -R a+w target

USER daemon

ENTRYPOINT ["java", "-jar", "backend.jar"]

EXPOSE 2552

COPY target/scala-2.12/backend.jar .
