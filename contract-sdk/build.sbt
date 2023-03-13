version := "2.0.2-akka"
organization := "gRPC-rfc"
scalaVersion := "2.13.10"
name := "contract-sdk"
crossPaths := false //stops sbt from appending SBT version to artifact name

lazy val akkaVersion = "2.7.0"
lazy val akkaHttpVersion = "10.5.0"
lazy val akkaGrpcVersion = "2.3.0"

enablePlugins(AkkaGrpcPlugin)

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-pki" % akkaVersion,

//  // The Akka HTTP overwrites are required because Akka-gRPC depends on 10.1.x
//  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
//  "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion,
)

// will get published as gRPC-rfc:contract-sdk:version
publishTo := Some("GAR" at "artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo")


