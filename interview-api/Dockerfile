# ===== Build =====
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Djava.net.preferIPv4Stack=true"

COPY gradlew ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew \
    && ./gradlew dependencies --no-daemon || ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar --no-daemon -x test \
    && find build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} /workspace/app.jar \;

# ===== Runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system spring \
    && useradd --system --gid spring --no-create-home spring \
    && mkdir -p /app/data /app/logs \
    && chown -R spring:spring /app

COPY --from=builder --chown=spring:spring /workspace/app.jar app.jar

USER spring
VOLUME ["/app/data", "/app/logs"]

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -fsS http://127.0.0.1:8080/interview-api/actuator/health || exit 1

ENV SPRING_PROFILES_ACTIVE=docker \
    TZ=Asia/Shanghai \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
