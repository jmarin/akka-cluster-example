package cluster

import akka.actor.ActorSystem
import api.FrontendApi
import com.typesafe.config.ConfigFactory

object FrontendMain {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [frontend]")
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    val system = ActorSystem(actorSystemName, config)
    system.actorOf(FrontendApi.props(), "frontend-api")
  }
}
