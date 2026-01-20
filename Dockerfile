# -------------- Build Stage (빌드 단계)--------------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

COPY src src
RUN ./gradlew clean bootJar -x test

# -------------- Run Stage (실행 단계)--------------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

ENV TZ=Asia/Seoul

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]