val scala3Version = "3.2.2"

lazy val txcell = project
  .in(file("."))
  .settings(
    name := "txcell",
    version := "0.1.0-SNAPSHOT",
    organization := "io",
    publishMavenStyle := true,

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )

val circeVersion = "0.14.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
