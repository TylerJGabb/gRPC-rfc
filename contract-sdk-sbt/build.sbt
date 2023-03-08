ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "contract-sdk-sbt"
  )

libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java" % "3.13.0" % "protobuf"
)

Compile / PB.targets := Seq(
  PB.gens.java -> (Compile / sourceManaged).value
)

