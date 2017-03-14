import akka.actor.ActorSystem
import api.FrontendApi
import com.typesafe.config.ConfigFactory

object FrontendMain {
  def main(args: Array[String]): Unit = {
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port")
      .withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    val system = ActorSystem(actorSystemName, config)
    system.actorOf(FrontendApi.props(), "frontend-api")
  }
}
