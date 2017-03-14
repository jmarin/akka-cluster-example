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
      }
    )
  )
  .dependsOn(seed, frontend, backend)
  .aggregate(seed, frontend, backend)

lazy val config = (project in file("config"))
  .settings(buildSettings:_*)

lazy val seed = (project in file("seed"))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "seed.jar",
      mainClass in assembly := Some("cluster.Main"),
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

lazy val frontend = (project in file("frontend"))
  .settings(buildSettings:_*)
  .settings(
    Seq(
      assemblyJarName in assembly := "frontend.jar",
      mainClass in assembly := Some("cluster.FrontendMain"),
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
      mainClass in assembly := Some("cluster.BackendMain"),
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


