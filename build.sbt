import sbt._
import Keys._
import sbtassembly.AssemblyPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import BuildSettings._
import Dependencies._


val clusterDeps = Seq(akkaCluster, akkaPubSub)
val testDeps = Seq(scalaTest, scalaCheck, akkaTestkit)

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
      libraryDependencies ++= clusterDeps
    )
  )
  .dependsOn(config)

lazy val web = (project in file("web"))
  .enablePlugins(ScalaJSPlugin)
  .settings(clusterBuildSettings)
  .settings(
    Seq(
      persistLauncher in Compile := true,
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.1",
        "com.github.japgolly.scalajs-react" %%% "core" % "1.0.0-RC1"
      ),
      jsDependencies ++= Seq(
        "org.webjars.bower" % "react" % "15.4.2" / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
        "org.webjars.bower" % "react" % "15.4.2" / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
        "org.webjars.bower" % "react" % "15.4.2" / "react-dom-server.js" minified  "react-dom-server.min.js" dependsOn "react-dom.js" commonJSName "ReactDOMServer"
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
      libraryDependencies ++= Seq(akkaHttp, akkaHttpJson) ++ clusterDeps,
      resourceGenerators in Compile += Def.task {
        val f1 = (fastOptJS in Compile in web).value
        val f2 = (packageScalaJSLauncher in Compile in web).value
        val f3 = (packageJSDependencies in Compile in web).value
        Seq(f1.data, f2.data, f3)
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
      libraryDependencies ++= clusterDeps ++ testDeps
    )
  )
  .dependsOn(config)


