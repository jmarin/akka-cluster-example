package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.{ KillYourself, ProcessLine, fileProcessingTopic }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Subscribe, SubscribeAck }

object ProcessorManager {
  case class FileDetails(lineCount: Int)
  def props(): Props = Props(new ProcessorManager)
  def createFileProcessor(system: ActorSystem): ActorRef = {
    system.actorOf(ProcessorManager.props())
  }
}

class ProcessorManager extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileProcessingTopic, self)

  override def receive: Receive = {
    case SubscribeAck(Subscribe(topic, None, `self`)) ⇒
      log.info(s"Subscribed to $topic")

    case msg: ProcessLine =>
      log.info(msg.toString)

    case KillYourself =>
      context stop self

  }
}
