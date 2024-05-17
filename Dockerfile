FROM openjdk:20
LABEL com.oconnellj2.image.authors="jdoconnell@pm.me"
LABEL version="1.0.0"
LABEL description="Spring Boot API providing RESTful services for a librarian"

ENV APP_HOME=/usr/src/java/com/oconnellj2/LibrarianAPI
WORKDIR $APP_HOME
COPY ./build/libs/LibrarianAPI-1.0.0.jar ./LibrarianAPI-1.0.0.jar
EXPOSE 8080
CMD ["java","-jar","LibrarianAPI-1.0.0.jar"]