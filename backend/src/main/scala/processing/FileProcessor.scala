package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.fileProcessingTopic
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Subscribe, SubscribeAck }

object FileProcessor {
  def props(): Props = Props(new FileProcessor)
  def createFileProcessor(system: ActorSystem): ActorRef = {
    system.actorOf(FileProcessor.props())
  }
}

class FileProcessor extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileProcessingTopic, self)

  override def receive: Receive = {
    case SubscribeAck(Subscribe(topic, None, `self`)) ⇒
      log.info(s"subscribing to $topic")

    case msg: String =>
      log.info(msg)

    case msg: Any =>
      log.info(msg.toString)
  }
}
