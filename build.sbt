import BuildSettings._
import Dependencies._
import sbt.Keys.libraryDependencies


lazy val akkaClusterExample = (project in file("."))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "akka-cluster.jar",
      assemblyMergeStrategy in assembly := {
        case "application.conf" => MergeStrategy.concat
        case "JS_DEPENDENCIES" => MergeStrategy.concat
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      mainClass in assembly := Some("cluster.AkkaCluster")
    )
  )
  .dependsOn(frontend, backend)
  .aggregate(frontend, backend)

lazy val config = (project in file("config"))
  .settings(buildSettings:_*)

lazy val frontend = (project in file("frontend"))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "frontend.jar",
      assemblyMergeStrategy in assembly := {
        case "application.conf" => MergeStrategy.concat
        case "JS_DEPENDENCIES" => MergeStrategy.concat
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      libraryDependencies ++= Seq(akkaHttp, akkaHttpJson, akkaCluster)
    )
  )
  .dependsOn(config)

lazy val backend = (project in file("backend"))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "backend.jar",
      assemblyMergeStrategy in assembly := {
        case "application.conf" => MergeStrategy.concat
        case "JS_DEPENDENCIES" => MergeStrategy.concat
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      libraryDependencies ++= Seq(akkaCluster)
    )
  )
  .dependsOn(config)


