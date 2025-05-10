ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "untitled"
  )

val Http4sVersion = "0.23.17"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % Http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe"        % Http4sVersion,
  "org.typelevel" %% "log4cats-slf4j"   % "2.6.0",
  "ch.qos.logback" % "logback-classic"  % "1.4.11"
)
