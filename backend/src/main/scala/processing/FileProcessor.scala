package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import common.CommonMessages.{ KillYourself, ProcessLine, fileProcessingTopic }

object FileProcessor {
  def props(fileName: String): Props = Props(new FileProcessor(fileName))
  def createFileProcessor(system: ActorSystem, fileName: String): ActorRef = {
    system.actorOf(FileProcessor.props(fileName))
  }
}

class FileProcessor(fileId: String) extends Actor with ActorLogging {

  var linesReceived = 0L
  val linesProcessed = 0L

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
    case processLine: ProcessLine =>
      linesReceived += 1
      log.info(linesReceived.toString)

    case KillYourself =>
      context stop self
  }
}
