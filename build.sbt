import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import BuildSettings._
import Dependencies._

lazy val akkaClusterExample = (project in file("."))
  .settings(clusterBuildSettings:_*)
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
  .aggregate(seed, frontend, backend, web)

lazy val config = (project in file("config"))
  .settings(clusterBuildSettings:_*)

lazy val seed = (project in file("seed"))
  .settings(clusterBuildSettings:_*)
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

lazy val web = (project in file("web"))
  .enablePlugins(ScalaJSPlugin)
  .settings(clusterBuildSettings)
  .settings(
    Seq(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.1"
      )
    )
  )

lazy val frontend = (project in file("frontend"))
  .settings(clusterBuildSettings:_*)
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
      libraryDependencies ++= Seq(akkaHttp, akkaHttpJson, akkaCluster),
      resourceGenerators in Compile += Def.task {
        val f1 = (fastOptJS in Compile in web).value
        val f2 = (packageScalaJSLauncher in Compile in web).value
        Seq(f1.data, f2.data)
      }.taskValue,
      watchSources ++= (watchSources in web).value
    )
  )
  .dependsOn(config)

lazy val backend = (project in file("backend"))
  .settings(clusterBuildSettings:_*)
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


