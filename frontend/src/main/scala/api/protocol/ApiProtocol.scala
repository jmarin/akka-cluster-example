package api.protocol

import spray.json.DefaultJsonProtocol
import api.model.{ FileUploaded, NodeDetails, Status }
import common.CommonMessages.FileDetails

trait ApiProtocol extends DefaultJsonProtocol {
  implicit val statusFormat = jsonFormat4(Status.apply)
  implicit val nodeDetailsFormat = jsonFormat4(NodeDetails.apply)
  implicit val fileUploadedFormat = jsonFormat1(FileUploaded.apply)
  implicit val fileDetailsFormat = jsonFormat2(FileDetails.apply)
}
