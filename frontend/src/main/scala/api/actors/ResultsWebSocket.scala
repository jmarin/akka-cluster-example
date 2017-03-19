package api.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{ Publish, Subscribe }
import common.CommonMessages._

object ResultsWebSocket {
  def props(): Props = Props(new ResultsWebSocket)
  def createResultsWebSocket(system: ActorSystem): ActorRef = {
    system.actorOf(ResultsWebSocket.props())
  }
}

class ResultsWebSocket extends Actor with ActorLogging {

  var totalWordCount = 0L
  var fileId = ""

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe(fileResultsTopic, self)

  override def receive: Receive = {

    case msg: EndProcessing =>
      fileId = msg.fileId

    //    case msg: WordStats =>
    //      totalWordCount += msg.wordCount
    //      self ! TotalWordCount(fileId, totalWordCount)

    case msg: TotalWordCount =>
      log.info(msg.toString)

  }

}
