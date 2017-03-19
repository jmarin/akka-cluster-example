package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Subscribe }
import common.CommonMessages._

object FileProcessor {
  def props(fileName: String, aggregator: ActorRef): Props = Props(new FileProcessor(fileName, aggregator))
  def createFileProcessor(system: ActorSystem, fileName: String, aggregator: ActorRef): ActorRef = {
    system.actorOf(FileProcessor.props(fileName, aggregator))
  }
}

class FileProcessor(fileId: String, aggregator: ActorRef) extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileProcessingTopic, self)

  override def preStart(): Unit = {
    super.preStart()
    log.info(s"Started processor ${self.path}")
  }

  override def postStop(): Unit = {
    super.postStop()
    log.info(s"Stopped processor ${self.path}")
  }

  override def receive: Receive = {
    case msg: ProcessLine =>
      mediator ! Publish(fileProcessingTopic, CountWords(msg.line))
      aggregator ! msg

    case KillYourself =>
      context stop self
  }

}
