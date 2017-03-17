package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import processing.CommonMessages._
import processing.LineProcessor.LineDetails
import processing.WordProcessor.WordStats

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object LineProcessor {
  case class LineDetails(lineCount: Long, wordCount: Long)
  def props(wordProcessor: ActorRef): Props = Props(new LineProcessor(wordProcessor))
  def createLineProcessor(system: ActorSystem, wordProcessor: ActorRef): ActorRef = {
    system.actorOf(LineProcessor.props(wordProcessor))
  }
}

class LineProcessor(wordProcessor: ActorRef) extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout = Timeout(5.seconds)

  override def receive: Receive = {
    case ProcessLine(line) =>
      val origin = sender()
      val fWords = (wordProcessor ? ProcessLine(line)).mapTo[WordStats]
      val fLineDetails = for {
        wordStats <- fWords
        lineDetails = LineDetails(1, wordStats.count)
      } yield lineDetails

      fLineDetails pipeTo origin

    case KillYourself => context stop self
  }
}
