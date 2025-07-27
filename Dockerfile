# ====================
# Stage 1: Build stage
# ====================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# gradle wrapper 및 의존성 캐시 관련 파일 복사
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew
RUN chmod +x ./gradlew

# ✅ 전체 소스 복사 후 빌드
COPY . .
RUN ./gradlew bootJar -x test --no-daemon --stacktrace

# 2) Runtime 스테이지: 표준 JRE (멀티아키 지원)
FROM eclipse-temurin:17-jre

WORKDIR /app

# 빌더에서 만든 JAR만 복사
COPY --from=builder /app/build/libs/haruapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Xms256m","-Xmx1024m","-jar","app.jar"]
