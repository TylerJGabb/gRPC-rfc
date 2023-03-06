import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


//source: https://github.com/google/protobuf-gradle-plugin/blob/master/examples/exampleKotlinDslProject/build.gradle.kts

plugins {
	id("org.springframework.boot") version "2.7.9"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"


	id("com.google.protobuf") version "0.8.19"
}

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.protobuf:protobuf-java:3.6.1")
	implementation("io.grpc:grpc-stub:1.15.1")
	implementation("io.grpc:grpc-protobuf:1.15.1")

	if (JavaVersion.current().isJava9Compatible) {
		// Workaround for @javax.annotation.Generated
		// see: https://github.com/grpc/grpc-java/issues/3633
		implementation("javax.annotation:javax.annotation-api:1.3.1")
	}

	testImplementation("junit:junit:4.12")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

//becomes available through plugins ANDfs dependencies
protobuf {
	protoc {
		// The artifact spec for the Protobuf Compiler
		artifact = "com.google.protobuf:protoc:3.6.1"
	}
	plugins {
		// Optional: an artifact spec for a protoc plugin, with "grpc" as
		// the identifier, which can be referred to in the "plugins"
		// container of the "generateProtoTasks" closure.
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.15.1"
		}
	}
	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				// Apply the "grpc" plugin whose spec is defined above, without
				// options. Note the braces cannot be omitted, otherwise the
				// plugin will not be added. This is because of the implicit way
				// NamedDomainObjectContainer binds the methods.
				id("grpc") { }
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
