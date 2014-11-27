name := "FilmSched"

version := "0.0.1"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "1.6.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "io.spray" %%  "spray-json" % "1.3.1",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.6.4")

scalacOptions += "-deprecation"
