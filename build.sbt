import BuildSettings._
import Dependencies._


lazy val akkaClusterExample = (project in file("."))
  .settings(buildSettings:_*)
  .aggregate(frontend, backend)

lazy val frontend = (project in file("frontend"))
  .settings(buildSettings:_*)
  .settings(
    libraryDependencies ++= Seq(akkaHttp, akkaHttpJson, akkaCluster)
  )

lazy val backend = (project in file("backend"))
  .settings(buildSettings:_*)
  .settings(
    libraryDependencies ++= Seq(akkaCluster)
  )


