package api

import akka.actor.{ ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.pattern.pipe
import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import api.actors.{ ClusterListener, FileReceiver }
import api.http.{ HttpApi, Service }
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ ExecutionContext, Future }

object FrontendApi {
  def props(): Props = Props(new FrontendApi)
}

class FrontendApi extends HttpApi with Service {
  override val name: String = "frontend-api"

  val config = ConfigFactory.load()
  override val log: LoggingAdapter = Logging(context.system, getClass)

  override val host: String = config.getString("frontend.host")
  override val port: Int = config.getInt("frontend.port")

  override implicit val system: ActorSystem = context.system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val ec: ExecutionContext = context.dispatcher

  //Start up actors
  val clusterListener = system.actorOf(ClusterListener.props())
  val fileReceiver = system.actorOf(FileReceiver.props())

  val mediator = DistributedPubSub(system).mediator

  override val paths: Route = routes(s"$name", clusterListener, mediator)
  override val http: Future[Http.ServerBinding] = Http(system).bindAndHandle(
    paths,
    host,
    port
  )

  http pipeTo self

}
