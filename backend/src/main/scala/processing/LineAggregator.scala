package processing

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import processing.CommonMessages._
import processing.LineAggregator.LineDetails
import processing.WordAggregator.WordStats

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object LineAggregator {
  case class LineDetails(lineNumber: Long, wordCount: Long)
  def props(): Props = Props(new LineAggregator)
  def createLineAggregator(system: ActorSystem): ActorRef = {
    system.actorOf(LineAggregator.props())
  }
}

class LineAggregator extends Actor with ActorLogging {

  var lineId = 0L

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout = Timeout(5.seconds)

  val wordAggregator = context.actorOf(WordAggregator.props())

  override def receive: Receive = {
    case ProcessLine(line) =>
      val origin = sender()
      lineId += 1
      val fWords = (wordAggregator ? ProcessLine(line)).mapTo[WordStats]
      val fLineDetails = for {
        wordStats <- fWords
        lineDetails = LineDetails(lineId, wordStats.count)
      } yield lineDetails

      fLineDetails pipeTo origin

    case KillYourself => context stop self
  }
}
