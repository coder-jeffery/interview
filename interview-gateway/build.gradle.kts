plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.easy"
version = "0.0.1-SNAPSHOT"
description = "interview-gateway"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

extra["springCloudVersion"] = "2025.1.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    // Gateway 基于 WebFlux/Netty，不能和 spring-boot-starter-webmvc 共存
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux")
    // Source: https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2025.1.0.0")
    // Source: https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-config
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:2025.1.0.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
