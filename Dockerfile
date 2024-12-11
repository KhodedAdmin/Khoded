FROM gradle:8.9.0-jdk-alpine AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY server/build.gradle.kts build.gradle.kts settings.gradle.kts local.properties gradle.properties /home/app/
COPY gradle /home/app/gradle
#COPY --chown=gradle:gradle . /home/app
WORKDIR /home/app
RUN gradle clean build -i --stacktrace

# Stage 2: Build Application
FROM gradle:8.9.0-jdk-alpine AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY ./server  /usr/src/app/
WORKDIR /usr/src/app
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Build the fat JAR, Gradle also supports shadow
# and boot JAR by default.
RUN gradle :server:buildFatJar --no-daemon

# Stage 3: Create the Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/khodedBackend.jar
ENTRYPOINT ["java","-jar","/app/khodedBackend.jar"]