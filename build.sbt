name := "FilmSched"

version := "0.0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "0.6.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "net.liftweb" %% "lift-json" % "2.5.1",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.6.4")

scalacOptions += "-deprecation"
