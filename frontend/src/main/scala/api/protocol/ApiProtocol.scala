package api.protocol

import spray.json.DefaultJsonProtocol
import api.model.{ NodeDetails, Status }

trait ApiProtocol extends DefaultJsonProtocol {
  implicit val statusFormat = jsonFormat4(Status.apply)
  implicit val nodeDetailsFormat = jsonFormat4(NodeDetails.apply)
}
