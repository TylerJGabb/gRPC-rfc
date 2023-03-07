import com.google.protobuf.gradle.*

// how did I get this to compile protobuf
// https://github.com/google/protobuf-gradle-plugin/blob/master/examples/exampleKotlinDslProject/build.gradle.kts

plugins {
	kotlin("jvm") version "1.6.21"
	id("com.google.protobuf") version "0.8.19"
	`maven-publish`
	id("com.google.cloud.artifactregistry.gradle-plugin") version "2.1.5"
}

repositories {
	mavenCentral()
	maven {
		url = uri("artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo")
	}

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
}

//becomes available through plugins AND dependencies
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

publishing {
	repositories {
		maven {
			url = uri("artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo")
		}
	}
	publications {
		create<MavenPublication>("publicationForLib") {
			group = "tg-group"
			artifactId = "contract-sdk" //how to control this via environment variable or semver
			from(components["java"])
		}
	}
}

