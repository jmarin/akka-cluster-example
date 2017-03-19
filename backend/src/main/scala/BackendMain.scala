package cluster

import akka.actor.ActorSystem
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings }
import com.typesafe.config.ConfigFactory
import common.CommonMessages.KillYourself
import processing.{ ProcessorManager, SampleActor, WordCountAggregator, WordCounter }

object BackendMain {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [backend]")
      .withFallback(ConfigFactory.load())
    val actorSystemName = config.getString("cluster.name")
    val system = ActorSystem(actorSystemName, config)
    val actor = system.actorOf(SampleActor.props(), "sample-actor")
    actor ! "Hello Backend"

    system.actorOf(WordCounter.props(), "word-counter")
    val aggregator = system.actorOf(WordCountAggregator.props(), "word-count-aggregator")

    system.actorOf(
      ClusterSingletonManager.props(
        singletonProps = ProcessorManager.props(aggregator),
        terminationMessage = KillYourself,
        settings = ClusterSingletonManagerSettings(system).withRole("backend")
      ), "file-processor"
    )
  }
}
