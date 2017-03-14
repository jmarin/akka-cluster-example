name := "akka-cluster-example"

organization := "Marin Incorporated"

scalaVersion := "2.12.1"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-target:jvm-1.8",
  "-feature",
  "-encoding", "UTF-8"
)

val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
)

