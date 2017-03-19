package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Subscribe }
import common.CommonMessages.{ CountWords, WordStats, fileProcessingTopic, fileResultsTopic }

object WordCounter {
  def props(): Props = Props(new WordCounter)
  def createWordCounter(system: ActorSystem): ActorRef = {
    system.actorOf(WordCounter.props())
  }
}

class WordCounter extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileProcessingTopic, self)

  override def receive: Receive = {
    case CountWords(line) =>
      val words = line.split(" ").length
      //log.info(s"Counting words: $words")
      mediator ! Publish(fileResultsTopic, WordStats(words))
  }
}
