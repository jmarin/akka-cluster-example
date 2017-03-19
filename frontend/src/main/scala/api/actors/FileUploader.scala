package api.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Subscribe }
import api.actors.FileUploader.{ CheckStatus, Uploaded }
import common.CommonMessages._

object FileUploader {
  case class Uploaded(lines: Int)
  case object CheckStatus
  def props(fileId: String): Props = Props(new FileUploader(fileId))
  def createFileUploader(system: ActorSystem, uploadId: String): ActorRef = {
    system.actorOf(FileUploader.props(uploadId))
  }
}

class FileUploader(fileId: String) extends Actor with ActorLogging {

  var totalWords = 0L
  var isProcessed = false

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileResultsTopic, self)

  override def receive: Receive = {

    case startProcessing: StartProcessing =>
      mediator ! Publish(fileProcessingTopic, startProcessing)

    case processLine: ProcessLine =>
      //mediator ! Publish(fileProcessingTopic, processLine)
      sender() ! Received

    case msg: EndProcessing =>
      //mediator ! Publish(fileProcessingTopic, msg)
      sender() ! Uploaded
      context stop self

  }
}
