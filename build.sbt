organization  := "com.spinoco"

version       := "0.1"

scalaVersion  := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

name := "Spinoco coding dojo"

version := "0.1.0"

scalaVersion := "2.10.3"

organization := "com.spinoco"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.scalaz.stream" %% "scalaz-stream" % "0.3.1",
  "org.specs2" %% "specs2" % "2.3.4" % "test"
)

mainClass := Some("com.spinoco.dojo.Main")

scalacOptions ++= Seq("-unchecked", "-deprecation")
