package api.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Send }
import api.actors.FileUploader.Uploaded

object FileUploader {
  case class Uploaded(lines: Int)
  def props(fileId: String): Props = Props(new FileUploader(fileId))
  def createFileUploader(system: ActorSystem, uploadId: String): ActorRef = {
    system.actorOf(FileUploader.props(uploadId))
  }
}

class FileUploader(fileId: String) extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Receive = {

    case startProcessing: StartProcessing =>
      mediator ! Publish(fileProcessingTopic, startProcessing)

    case line: ProcessLine =>
      mediator ! Publish(fileProcessingTopic, line)
      sender() ! Received

    case msg: EndProcessing =>
      mediator ! Publish(fileProcessingTopic, msg)
      sender() ! Uploaded
      context stop self
  }
}
