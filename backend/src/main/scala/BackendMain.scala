package cluster

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import processing.{ FileProcessor, SampleActor }

object BackendMain {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [backend]")
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    val system = ActorSystem(actorSystemName, config)
    val actor = system.actorOf(SampleActor.props(), "sample-actor")
    actor ! "Hello Backend"
    system.actorOf(FileProcessor.props(), "file-processor")

  }
}
