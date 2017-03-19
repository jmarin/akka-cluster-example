package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Subscribe, SubscribeAck }

object ProcessorManager {
  def props(aggregator: ActorRef): Props = Props(new ProcessorManager(aggregator))
  def createFileProcessor(system: ActorSystem, aggregator: ActorRef): ActorRef = {
    system.actorOf(ProcessorManager.props(aggregator))
  }
}

class ProcessorManager(aggregator: ActorRef) extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileProcessingTopic, self)

  var processors: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case SubscribeAck(Subscribe(topic, None, `self`)) ⇒
      log.info(s"Subscribed to $topic")

    case msg: StartProcessing =>
      val processor = context.actorOf(FileProcessor.props(msg.fileId, aggregator), msg.fileId)
      processors = processors + ((msg.fileId, processor))
      println(processors)

    case msg: EndProcessing =>
      processors.find(x => x._1 == msg.fileId).foreach(x => x._2 ! KillYourself)
      processors = processors.filter(x => x._1 != msg.fileId)
      println(processors)

    case KillYourself =>
      context stop self

  }
}
