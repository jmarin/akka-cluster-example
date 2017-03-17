package cluster

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [seed]")
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    ActorSystem(actorSystemName, config)
  }
}
