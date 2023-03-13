version := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "server-sbt"
  )

lazy val akkaVersion = "2.7.0"
lazy val akkaHttpVersion = "10.5.0"
lazy val akkaGrpcVersion = "2.3.0"

fork := true

resolvers += "My private artifacts" at "artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo"

libraryDependencies ++= Seq(
  "gRPC-rfc" % "contract-sdk" % "2.0.2-akka",
  "io.grpc" % "grpc-services" % "1.53.0",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-pki" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",

)

