package processing

import akka.testkit.TestProbe
import processing.CommonMessages.ProcessLine
import processing.WordAggregator._

class WordAggregatorSpec extends ActorSpec {

  val wordAggregator = createWordAggregator(system)

  val probe = TestProbe()

  "A Word Aggregator" must {
    "Count words in line of text" in {
      val text = "This is a text"
      probe.send(wordAggregator, ProcessLine(text))
      probe.expectMsg(WordStats(4L))
    }
  }

}
