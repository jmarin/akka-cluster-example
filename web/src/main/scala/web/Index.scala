package web

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import japgolly.scalajs.react._

object Index extends js.JSApp {

  val HelloMessage = ScalaComponent.build[String]("HelloMessage")

  @JSExport
  override def main(): Unit = {
    println("Hello Scala.js")
  }
}
