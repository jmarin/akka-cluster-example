package common

object CommonMessages {
  case object Received
  case object GetState
  case object KillYourself
  case class ProcessLine(line: String)
  case class StartProcessing(fileId: String)
  case class EndProcessing(fileId: String)
  case class CountWords(line: String)
  case class WordStats(wordCount: Long)
  case class TotalWordCount(fileId: String, count: Long)
  case class FileDetails(fileName: String, wordCount: Long)
  val fileProcessingTopic = "fileProcessing"
  val fileResultsTopic = "fileResults"
}
