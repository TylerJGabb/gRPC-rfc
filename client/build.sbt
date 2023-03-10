ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "client-sbt"
  )

resolvers += "My private artifacts" at "artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo"

libraryDependencies ++= Seq(
  "gRPC-rfc" % "contract-sdk" % "1.0.1",
  "io.grpc" % "grpc-core" % "1.53.0"
)