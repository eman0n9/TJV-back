FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY . .
RUN chmod +x ./gradlew && ./gradlew clean bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /src/build/libs/*.jar /app/app.jar

ENV TZ=Europe/Prague \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
