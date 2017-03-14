import sbt._

object Dependencies {

  val repos = Seq(
    "Local Maven Repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    "Typesafe Repo" at "api.http://repo.typesafe.com/typesafe/releases/",
    "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"
  )

  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % Version.akkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttpVersion
  val akkaHttpJson = "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttpVersion

}
