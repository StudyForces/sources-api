FROM openjdk:16-jdk-alpine as build

WORKDIR /build
COPY . .
RUN /build/gradlew clean build -x test

FROM openjdk:16-jdk-alpine

WORKDIR /app
COPY --from=build /build/build/libs/questions-service-0.0.1-SNAPSHOT.war /app/app.war
ENTRYPOINT ["java","-jar","/app/app.war"]
