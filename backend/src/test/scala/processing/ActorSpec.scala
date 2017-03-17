package processing

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, MustMatchers, WordSpec }

class ActorSpec extends WordSpec with MustMatchers with BeforeAndAfterAll {

  val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=0")
    .withFallback(ConfigFactory.load())

  implicit val system = ActorSystem("test", config)

  override def afterAll(): Unit = {
    println("Terminating system")
    system.terminate()
  }

}
