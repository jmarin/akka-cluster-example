import sbt._

object Dependencies {

  val repos = Seq(
    "Local Maven Repo" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    "Typesafe Repo" at "api.http://repo.typesafe.com/typesafe/releases/",
    "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"
  )

  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % "test"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck % "test"
  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % Version.akkaVersion
  val akkaPubSub = "com.typesafe.akka" %% "akka-cluster-tools" % Version.akkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttpVersion
  val akkaHttpJson = "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttpVersion
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % Version.akkaVersion % "test"
  val constructr = "de.heikoseeberger" %% "constructr" % Version.constructrVersion
  val constructrZookeeper = "com.lightbend.constructr" %% "constructr-coordination-zookeeper" % Version.constructrZookeeperVersion

}
