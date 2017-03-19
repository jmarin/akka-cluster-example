package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Put }
import common.CommonMessages._

object WordCounter {
  def props(): Props = Props(new WordCounter)
  def createWordCounter(system: ActorSystem): ActorRef = {
    system.actorOf(WordCounter.props())
  }
}

class WordCounter extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  mediator ! Put(self)

  override def receive: Receive = {
    case msg: ProcessLine =>
      val words = msg.line.split(" ").length
      log.debug(s"Counting words: $words")
      sender() ! words
  }
}
