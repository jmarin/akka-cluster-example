package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Subscribe }
import common.CommonMessages._

object FileProcessor {
  def props(fileName: String): Props = Props(new FileProcessor(fileName))
  def createFileProcessor(system: ActorSystem, fileName: String): ActorRef = {
    system.actorOf(FileProcessor.props(fileName))
  }
}

class FileProcessor(fileId: String) extends Actor with ActorLogging {

  var linesReceived = 0L
  var linesProcessed = 0L
  var totalWordCount = 0L

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
    case ProcessLine(line) =>
      linesReceived += 1
      log.debug(s"LINES RECEIVED: ${linesReceived.toString}")
      mediator ! Publish(fileProcessingTopic, CountWords(line))

    case wordStats: WordStats =>
      linesProcessed += 1
      log.debug(s"LINES PROCESSED: ${linesProcessed.toString}")
      totalWordCount += wordStats.wordCount
      checkPublishResults()

    case KillYourself =>
      context stop self
  }

  private def checkPublishResults(): Unit = {
    if (linesReceived == linesProcessed) {
      mediator ! Publish(fileResultsTopic, TotalWordCount(fileId, totalWordCount))
    }
  }
}
