package api.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.{ EndReceiving, ProcessLine, Received, fileProcessingTopic }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Send }
import api.actors.FileUploader.Uploaded

object FileUploader {
  case class Uploaded(lines: Int)
  def props(): Props = Props(new FileUploader)
  def createFileUploader(system: ActorSystem): ActorRef = {
    system.actorOf(FileUploader.props())
  }
}

class FileUploader extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Receive = {
    case line: ProcessLine =>
      mediator ! Publish(fileProcessingTopic, line)
      sender() ! Received

    case EndReceiving =>
      sender() ! Uploaded
  }
}
