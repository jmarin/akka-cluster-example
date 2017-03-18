package common

object CommonMessages {
  case object Received
  case object GetState
  case object KillYourself
  case class ProcessLine(line: String)
  case class StartProcessing(fileId: String)
  case class EndProcessing(fileId: String)
  case class WordStats(wordCount: Long)
  val fileProcessingTopic = "fileProcessing"
}
