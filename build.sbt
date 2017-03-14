import BuildSettings._
import Dependencies._


lazy val akkaClusterExample = (project in file("."))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "akka-cluster.jar"
    )
  )
  .aggregate(frontend, backend)

lazy val frontend = (project in file("frontend"))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "frontend.jar",
      libraryDependencies ++= Seq(akkaHttp, akkaHttpJson, akkaCluster)
    )
  )

lazy val backend = (project in file("backend"))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "backend.jar",
      libraryDependencies ++= Seq(akkaCluster)
    )
  )


