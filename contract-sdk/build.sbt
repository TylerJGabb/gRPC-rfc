ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "gRPC-rfc"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / name := "contract-sdk"
ThisBuild / crossPaths := false //stops sbt from appending SBT version to artifact name

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

// will get published as gRPC-rfc:contract-sdk:VERSION
publishTo := Some("GAR" at "artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo")


