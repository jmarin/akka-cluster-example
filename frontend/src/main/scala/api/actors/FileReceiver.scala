package api.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.{ ProcessLine, Received }

object FileReceiver {
  def props(): Props = Props(new FileReceiver)
  def createFileReceiver(system: ActorSystem): ActorRef = {
    system.actorOf(FileReceiver.props())
  }
}

class FileReceiver extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg: ProcessLine =>
      log.info(msg.toString)
      sender() ! Received
  }
}
