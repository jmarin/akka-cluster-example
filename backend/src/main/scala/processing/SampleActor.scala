package processing

import akka.actor.{ Actor, Props }

object SampleActor {
  def props(): Props = Props(new SampleActor)
}

class SampleActor extends Actor {
  override def receive: Receive = {
    case msg => println(msg)
  }
}
