package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import common.CommonMessages.{ ProcessLine, WordStats }

object WordCounter {
  def props(): Props = Props(new WordCounter)
  def createWordCounter(system: ActorSystem): ActorRef = {
    system.actorOf(WordCounter.props())
  }
}

class WordCounter extends Actor with ActorLogging {
  override def receive: Receive = {
    case ProcessLine(line) =>
      val words = line.split(" ").length
      sender() ! WordStats(words)
  }
}
