package api.http

import java.net.InetAddress
import java.time.Instant

import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.Member
import akka.cluster.pubsub.DistributedPubSubMediator.Send
import akka.pattern.ask
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ HttpResponse, StatusCodes }
import spray.json._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{ Framing, Sink }
import akka.util.{ ByteString, Timeout }
import api.actors.ClusterListener.GetState
import api.model.{ FileUploaded, NodeDetails, Status }
import api.protocol.ApiProtocol
import common.CommonMessages.{ ProcessLine, Received }

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }
import scala.concurrent.duration._

trait Service extends ApiProtocol {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val ec: ExecutionContext
  val log: LoggingAdapter

  implicit val timeout: Timeout = Timeout(5.seconds)

  val splitLines = Framing.delimiter(ByteString("\n"), 2048, allowTruncation = true)

  def rootPath(mediator: ActorRef) = {
    get {
      pathSingleSlash {
        getFromResource("web/index.html")
      } ~
        path("web-jsdeps.js")(getFromResource("web-jsdeps.js")) ~
        path("web-fastopt.js")(getFromResource("web-fastopt.js")) ~
        path("web-launcher.js")(getFromResource("web-launcher.js")) ~
        path("main.css")(getFromResource("web/css/main.css")) ~
        path("main.js")(getFromResource("web/js/main.js"))
    } ~
      post {
        path("upload") {
          fileUpload("file") {
            case (metadata, byteSource) =>
              val startTime = Instant.now().toEpochMilli
              val uploadedF = byteSource
                .via(splitLines)
                .map(_.utf8String)
                .mapAsync(parallelism = 2)(line => (mediator ? Send("/user/word-counter", ProcessLine(line), false)).mapTo[Int])
                .runWith(Sink.fold[Int, Int](0) { (acc, x) => acc + x })

              onComplete(uploadedF) {
                case Success(total) =>
                  val endTime = Instant.now().toEpochMilli
                  val time = endTime - startTime
                  complete(ToResponseMarshallable(FileUploaded(metadata.fileName, total, time.toInt)))
                case Failure(error) =>
                  log.error(error.getLocalizedMessage)
                  complete(HttpResponse(StatusCodes.InternalServerError))
              }

          }
        }
      }
  }

  def statusPath(name: String) = {
    path("status") {
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
        val fDetails = for {
          members <- (clusterListener ? GetState).mapTo[Set[Member]]
          details = members.map(m => NodeDetails(m.status.toString, m.roles.toList, m.uniqueAddress.address.host.getOrElse(""), m.uniqueAddress.address.port.getOrElse(0)))
        } yield details
        onComplete(fDetails) {
          case Success(nodeListDetails) => complete(ToResponseMarshallable(nodeListDetails))
          case Failure(_) => complete(HttpResponse(StatusCodes.InternalServerError))
        }
      }
    }
  }

  def routes(apiName: String, clusterListener: ActorRef, fileReceiver: ActorRef) =
    encodeResponse(rootPath(fileReceiver) ~
      statusPath(apiName)) ~
      encodeResponse(clusterMembers(clusterListener))

}
