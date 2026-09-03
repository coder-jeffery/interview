plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.easy"
version = "0.0.1-SNAPSHOT"
description = "interview-web"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	maven("https://maven.aliyun.com/repository/public")
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	// 必须要有这个
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Source: https://mvnrepository.com/artifact/com.alibaba.fastjson2/fastjson2
	implementation("com.alibaba.fastjson2:fastjson2:2.0.64")
	// Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
	implementation("com.fasterxml.jackson.core:jackson-databind:2.22.2")
	// Source: https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java
	implementation("com.google.protobuf:protobuf-java:4.33.6")
	// Source: https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
	// Source: https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery
	implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2025.1.0.0")
	// Source: https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:5.0.3")
	// Source: https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-loadbalancer
	implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:5.0.2")
	// Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-jdbc
	implementation("org.springframework.boot:spring-boot-starter-jdbc:4.1.0")
	// Source: https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
	implementation("com.mysql:mysql-connector-j:9.7.0")
	// Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:4.1.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
