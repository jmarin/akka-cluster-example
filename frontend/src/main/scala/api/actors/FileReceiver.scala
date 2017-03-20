package api.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.{ ProcessLine, Received, fileProcessingTopic }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish

object FileReceiver {
  def props(): Props = Props(new FileReceiver)
  def createFileReceiver(system: ActorSystem): ActorRef = {
    system.actorOf(FileReceiver.props())
  }
}

class FileReceiver extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Receive = {
    case msg: ProcessLine =>
      mediator ! Publish(fileProcessingTopic, msg)
      sender() ! Received
  }
}
