package api.protocol

import spray.json.DefaultJsonProtocol
import api.model.Status

trait ApiProtocol extends DefaultJsonProtocol {
  implicit val statusFormat = jsonFormat4(Status.apply)
}
