package cluster

import akka.actor.ActorSystem
import akka.cluster.pubsub.DistributedPubSub
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [seed]")
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    val system = ActorSystem(actorSystemName, config)
    DistributedPubSub(system).mediator
  }
}
