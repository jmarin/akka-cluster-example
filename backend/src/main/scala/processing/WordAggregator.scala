package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import processing.CommonMessages.ProcessLine
import processing.WordAggregator.WordStats

object WordAggregator {
  case class WordStats(count: Long)
  def props(): Props = Props(new WordAggregator)
  def createWordAggregator(system: ActorSystem): ActorRef = {
    system.actorOf(WordAggregator.props())
  }
}

class WordAggregator extends Actor with ActorLogging {
  var wordCount: Long = 0L

  override def receive: Receive = {
    case ProcessLine(line) =>
      val words = line.split(" ")
      wordCount = words.length
      sender() ! WordStats(wordCount)
  }
}
