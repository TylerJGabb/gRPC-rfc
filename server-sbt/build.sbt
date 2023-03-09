ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "server-sbt"
  )

resolvers += "My private artifacts" at "artifactregistry://us-central1-maven.pkg.dev/a-proj-to-be-deleted/java-repo"

libraryDependencies ++= Seq(
  "contract-sdk-sbt" % "contract-sdk-sbt_2.13" % "0.1.0-SNAPSHOT"
)

