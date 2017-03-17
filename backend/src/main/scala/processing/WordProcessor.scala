package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import processing.CommonMessages.ProcessLine
import processing.WordProcessor.WordStats

object WordProcessor {
  case class WordStats(count: Long)
  def props(): Props = Props(new WordProcessor)
  def createWordProcessor(system: ActorSystem): ActorRef = {
    system.actorOf(WordProcessor.props())
  }
}

class WordProcessor extends Actor with ActorLogging {
  var wordCount: Long = 0L

  override def receive: Receive = {
    case ProcessLine(line) =>
      val words = line.split(" ")
      wordCount = words.length
      sender() ! WordStats(wordCount)
  }
}
