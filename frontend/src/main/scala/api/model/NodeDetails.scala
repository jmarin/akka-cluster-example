package api.model

case class NodeDetails(status: String, roles: List[String], host: String = "", port: Int = 0)
