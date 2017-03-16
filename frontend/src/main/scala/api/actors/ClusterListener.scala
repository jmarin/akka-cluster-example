package api.actors

import akka.actor.{ Actor, ActorLogging, Props }
import akka.cluster.ClusterEvent._
import akka.cluster.{ Cluster, Member }

object ClusterListener {
  def props(): Props = Props(new ClusterListener)
  case object GetState
}

class ClusterListener extends Actor with ActorLogging {
  import ClusterListener._

  val cluster = Cluster(context.system)

  var members = Set.empty[Member]

  override def preStart(): Unit = {
    super.preStart()
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = {
    super.postStop()
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case MemberUp(member) =>
      members = members + member
    case UnreachableMember(_) => //ignore

    case MemberRemoved(member, _) =>
      members = members - member

    case GetState =>
      sender() ! members
  }

}
