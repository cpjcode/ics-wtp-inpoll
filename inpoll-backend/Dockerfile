FROM maven:3.9.12-eclipse-temurin-25 AS build
COPY pom.xml /build/pom.xml
COPY src /build/src
WORKDIR /build/
RUN mvn package

FROM eclipse-temurin:25-jre
COPY --from=build /build/target/inpoll-backend-1.jar /app/inpoll-backend-1.jar
WORKDIR /app/
EXPOSE 8080
ENV DB_HOST=db
ENTRYPOINT ["java", "-jar", "inpoll-backend-1.jar"]