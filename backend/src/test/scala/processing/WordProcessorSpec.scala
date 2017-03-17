package processing

import akka.testkit.TestProbe
import processing.CommonMessages.ProcessLine
import processing.WordProcessor._

class WordProcessorSpec extends ActorSpec {

  val wordProcessor = createWordProcessor(system)

  val probe = TestProbe()

  "A Word Aggregator" must {
    "Count words in line of text" in {
      val text = "This is a text"
      probe.send(wordProcessor, ProcessLine(text))
      probe.expectMsg(WordStats(4L))
    }
  }

}
