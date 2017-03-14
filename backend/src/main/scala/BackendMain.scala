package cluster

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import processing.SampleActor

object BackendMain {
  def main(args: Array[String]): Unit = {
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    val system = ActorSystem(actorSystemName, config)
    val actor = system.actorOf(SampleActor.props(), "sample-actor")
    actor ! "Hello Backend"
  }
}
