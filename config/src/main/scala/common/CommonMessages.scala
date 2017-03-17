package common

object CommonMessages {
  case object Received
  case object GetState
  case object KillYourself
  case class ProcessLine(line: String)
  case object EndReceiving
  val fileProcessingTopic = "fileProcessing"
}
