package common

object CommonMessages {
  case object Received
  case object GetState
  case object KillYourself
  case class ProcessLine(line: String)
  case object EndReceiving
  case class WordStats(wordCount: Long)
  val fileProcessingTopic = "fileProcessing"
}
