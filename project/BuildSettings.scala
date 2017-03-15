import sbt._
import sbt.Keys._

object BuildSettings {
  val buildOrganization = "Marin Incorporated"
  val buildVersion = "1.0.0"
  val buildScalaVersion = "2.12.1"

  val clusterBuildSettings = Defaults.coreDefaultSettings ++
   Seq(
     organization := buildOrganization,
     version := buildVersion,
     scalaVersion := buildScalaVersion,
     scalacOptions ++= Seq(
       "-unchecked",
       "-deprecation",
       "-target:jvm-1.8",
       "-feature",
       "-encoding", "UTF-8"
     ),
     parallelExecution in Test := false
   )
}


