# ===== Build =====
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Djava.net.preferIPv4Stack=true -Dhttps.protocols=TLSv1.2,TLSv1.3"

COPY gradlew gradle.properties ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew \
    && (./gradlew dependencies --no-daemon \
        || (sleep 8 && ./gradlew dependencies --no-daemon) \
        || (sleep 8 && ./gradlew dependencies --no-daemon))

COPY src src
RUN (./gradlew bootJar --no-daemon -x test \
        || (sleep 8 && ./gradlew bootJar --no-daemon -x test) \
        || (sleep 8 && ./gradlew bootJar --no-daemon -x test)) \
    && find build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} /workspace/app.jar \;

# ===== Runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system spring \
    && useradd --system --gid spring --no-create-home spring \
    && mkdir -p /app/logs \
    && chown -R spring:spring /app

COPY --from=builder --chown=spring:spring /workspace/app.jar app.jar
USER spring
VOLUME ["/app/logs"]

EXPOSE 8080

# 单实例：Xms=Xmx 避免运行时扩缩堆停顿；G1 为 JDK 9+ 默认收集器
# 容器建议 --memory >= 1g（堆 + 元空间 + 线程栈 + 堆外）
ENV LOG_PATH=/app/logs \
    JAVA_OPTS="-Xms512m -Xmx512m -Xss512k -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs -Xlog:gc*:file=/app/logs/gc.log:time,uptime,level,tags:filecount=5,filesize=20M" \
    SPRING_PROFILES_ACTIVE=dev \
    TZ=Asia/Shanghai

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
