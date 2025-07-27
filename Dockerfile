# ====================
# Stage 1: Build stage
# ====================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# ✅ Gradle Wrapper 복사 (gradlew, gradle 디렉토리 포함!)
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# ✅ 실행 권한 부여
RUN chmod +x gradlew

# ✅ 의존성 캐시
RUN ./gradlew dependencies --no-daemon || true

# ✅ 전체 소스 복사 후 빌드
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# 2) Runtime 스테이지: 표준 JRE (멀티아키 지원)
FROM eclipse-temurin:17-jre

WORKDIR /app

# 빌더에서 만든 JAR만 복사
COPY --from=builder /app/build/libs/haruapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Xms256m","-Xmx1024m","-jar","app.jar"]
