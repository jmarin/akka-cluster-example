package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.{ KillYourself, ProcessLine, fileProcessingTopic }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Subscribe, SubscribeAck }

object FileProcessor {
  case class FileDetails(lineCount: Int)
  def props(): Props = Props(new FileProcessor)
  def createFileProcessor(system: ActorSystem): ActorRef = {
    system.actorOf(FileProcessor.props())
  }
}

class FileProcessor extends Actor with ActorLogging {

  var lineCount: Int = 0

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileProcessingTopic, self)

  override def receive: Receive = {
    case SubscribeAck(Subscribe(topic, None, `self`)) ⇒
      log.info(s"Subscribed to $topic")

//    case msg: ProcessLine =>
//      log.info(msg.toString)
//      lineCount += 1

    case KillYourself =>
      context stop self

  }
}
