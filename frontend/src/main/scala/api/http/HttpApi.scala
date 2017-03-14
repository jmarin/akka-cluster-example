package api.http

import java.net.InetSocketAddress

import akka.actor.{ Actor, ActorSystem, Status }
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.{ ExecutionContext, Future }

abstract class HttpApi extends Actor {

  val log: LoggingAdapter
  val path = self.path

  val name: String

  val host: String
  val port: Int

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val ec: ExecutionContext

  val paths: Route
  val http: Future[ServerBinding]

  override def preStart(): Unit = {
    super.preStart()
    log.info(s"Started $path")
  }

  override def postStop(): Unit = {
    super.postStop()
    log.info(s"Stopped $path")
  }

  override def receive: Receive = {
    case Http.ServerBinding(s) => handleServerBinding(s)
    case Status.Failure(e) => handleBindFailure(e)
  }

  private def handleServerBinding(address: InetSocketAddress): Unit = {
    log.info(s"$name started on {}", address)
    context.become(Actor.emptyBehavior)
  }

  private def handleBindFailure(error: Throwable): Unit = {
    log.error(s"Failed to bind to $host:$port")
    context stop self
  }

}
