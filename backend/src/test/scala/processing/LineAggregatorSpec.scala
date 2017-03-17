package processing

import akka.testkit.TestProbe
import processing.CommonMessages.{ GetState, ProcessLine }
import processing.LineAggregator._
import processing.TestData._

class LineAggregatorSpec extends ActorSpec {

  val lineAggregator = createLineAggregator(system)

  val probe = TestProbe()

  "A Line Aggregator" must {
    "Provide line details from text" in {
      val text = "This is a text"
      probe.send(lineAggregator, ProcessLine(text))
      probe.expectMsg(LineDetails(1, 4))
    }
  }

}
