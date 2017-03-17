package processing

object CommonMessages {
  case object GetState
  case object KillYourself
  case class ProcessLine(line: String)
}
