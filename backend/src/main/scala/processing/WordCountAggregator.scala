package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Subscribe }
import common.CommonMessages._

object WordCountAggregator {
  def props(): Props = Props(new WordCountAggregator)
  def createWordCountAggregator(system: ActorSystem): ActorRef = {
    system.actorOf(WordCountAggregator.props())
  }
}

class WordCountAggregator extends Actor with ActorLogging {

  var linesReceived = 0L
  var linesProcessed = 0L
  var totalWordCount = 0L
  var fileId = ""

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileResultsTopic, self)

  override def preStart(): Unit = {
    super.preStart()
    log.info(s"started aggregator ${self.path}")
  }

  override def receive: Receive = {
    case msg: ProcessLine =>
      linesReceived += 1

    case wordStats: WordStats =>
      linesProcessed += 1
      totalWordCount += wordStats.wordCount
      if (linesProcessed == linesReceived) {
        println(s"Total Count: $totalWordCount")
        mediator ! Publish(fileResultsTopic, TotalWordCount(fileId, totalWordCount))
      }

  }
}
