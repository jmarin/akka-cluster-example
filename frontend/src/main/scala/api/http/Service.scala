package api.http

import java.net.InetAddress
import java.time.Instant

import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.Member
import akka.pattern.ask
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ HttpResponse, StatusCodes }
import spray.json._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import api.actors.ClusterListener.GetState
import api.model.Status
import api.protocol.ApiProtocol

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }
import scala.concurrent.duration._

trait Service extends ApiProtocol {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val ec: ExecutionContext
  val log: LoggingAdapter

  implicit val timeout: Timeout = Timeout(5.seconds)

  def rootPath(name: String) = {
    pathSingleSlash {
      get {
        complete {
          val now = Instant.now.toString
          val host = InetAddress.getLocalHost.getHostName
          val status = Status("OK", name, now, host)
          log.debug(status.toJson.toString)
          ToResponseMarshallable(status)
        }
      }
    }
  }

  def clusterMembers(clusterListener: ActorRef) = {
    path("nodes") {
      get {
        val fMembers = for {
          xs <- (clusterListener ? GetState).mapTo[Set[Member]]
          addresses = xs.map(m => m.address.toString)
        } yield addresses
        onComplete(fMembers) {
          case Success(addresses) => complete(ToResponseMarshallable(addresses))
          case Failure(_) => complete(HttpResponse(StatusCodes.InternalServerError))
        }
      }
    }
  }

  def routes(apiName: String, clusterListener: ActorRef) = encodeResponse(rootPath(apiName)) ~ encodeResponse(clusterMembers(clusterListener))

}
