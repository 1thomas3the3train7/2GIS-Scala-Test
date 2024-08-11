ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "2GISTest"
  )

javacOptions ++= Seq(
  "-source", "11",
  "-target", "11"
)

val http4sVersion = "0.23.16"
val circeVersion = "0.14.9"
val doobieVersion = "1.0.0-RC2"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "com.typesafe" % "config" % "1.4.3",
  "com.github.pureconfig" %% "pureconfig" % "0.17.7",
  "org.jsoup" % "jsoup" % "1.18.1",
  "co.fs2" %% "fs2-core" % "3.10.2",
  "org.typelevel" %% "log4cats-slf4j" % "2.7.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test
)