package processing

import akka.testkit.TestProbe
import WordCounter._
import common.CommonMessages.{ ProcessLine, WordStats }

class WordCounterSpec extends ActorSpec {

  val probe = TestProbe()

  val wordCounter = createWordCounter(system)

  "A Word Counter" must {
    "count words in text" in {
      val text = "There are words in this text"
      val expectedSize = text.split(" ").length
      probe.send(wordCounter, ProcessLine(text))
      probe.expectMsg(WordStats(expectedSize))
    }
  }
}
