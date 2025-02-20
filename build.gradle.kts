plugins {
	kotlin("jvm") version "2.1.0"
	kotlin("kapt") version "2.1.0"
	kotlin("plugin.spring") version "2.1.0"
	kotlin("plugin.jpa") version "2.1.0"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.asciidoctor.jvm.convert") version "4.0.4"
	id("com.epages.restdocs-api-spec") version "0.19.4"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		jvmToolchain(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")

    // DB
	runtimeOnly("com.mysql:mysql-connector-j")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testImplementation("com.redis:testcontainers-redis:2.2.2")
	testImplementation("org.testcontainers:kafka")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

	// ObjectMapper
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Instancio
	testImplementation("org.instancio:instancio-junit:5.2.1")

	// Spring REST Docs
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	// Swagger UI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")

	// OpenAPI Specification
	testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.4")

	// QueryDSL
	implementation("com.querydsl:querydsl-jpa:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")
	kapt("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")

	// Redisson
	implementation("org.redisson:redisson-spring-boot-starter:3.43.0")

	// Kafka
	implementation("org.springframework.kafka:spring-kafka")
}

// OpenAPI Specification
configure<com.epages.restdocs.apispec.gradle.OpenApi3Extension> {
	setServer("http://localhost:8080")
	title = "콘서트 예약 서비스 API"
	description = "콘서트 예약 서비스 API 명세서"
	version = getGitHash()
	format = "json"
	outputDirectory = "build/resources/main/static"
}

tasks {
	bootJar {
		dependsOn(":openapi3")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}
