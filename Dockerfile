# ====================
# Stage 1: Build stage
# ====================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Gradle Wrapper 복사 및 실행 권한 부여
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew

# 의존성 캐시
RUN ./gradlew dependencies --no-daemon || true

# 전체 소스 복사
COPY . .

# JAR 빌드
RUN ./gradlew clean build -x test --no-daemon
# =====================
# Stage 2: Runtime stage
# =====================
FROM eclipse-temurin:17-jdk AS runner

WORKDIR /app

# 빌드한 JAR 복사
COPY --from=builder /app/build/libs/haruapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]