package processing

import akka.testkit.TestProbe
import processing.CommonMessages.ProcessLine
import processing.LineProcessor._
import processing.WordProcessor.createWordProcessor

class LineProcessorSpec extends ActorSpec {

  val wordProcessor = createWordProcessor(system)
  val lineProcessor = createLineProcessor(system, wordProcessor)

  val probe = TestProbe()

  "A Line Processor" must {
    "Provide line details from text" in {
      val text = "This is a text"
      probe.send(lineProcessor, ProcessLine(text))
      probe.expectMsg(LineDetails(1, 4))
    }
  }

}
