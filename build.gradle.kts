plugins {
	id("java")
	id("jacoco")
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("info.solidsoft.pitest") version "1.15.0"
}

group = "com.oconnellj2"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_20
repositories.mavenCentral()

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	// Spring.
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// H2.
	implementation("com.h2database:h2")
	// OpenAPI Swagger documentation.
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	// Annotations/Configuration Processor.
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	// JUnit PIT Mutation testing.
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	pitest("org.pitest:pitest-junit5-plugin:1.2.1")
	// Monitoring.
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	// Docker.
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

jacoco.toolVersion = "0.8.12"
tasks.jacocoTestReport {
	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it) {
			setExcludes(listOf(
				"**/config/**",
				"**/Application.class"
			))
		}
	}))
}
pitest {
	pitestVersion.set("1.16.1")
	excludedClasses.set(listOf("**.config.**"))
}
tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}