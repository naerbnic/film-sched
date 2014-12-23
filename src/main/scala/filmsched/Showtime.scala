package filmsched

import org.joda.time.DateTime
import spray.json._
import org.joda.time.format.DateTimeFormat

import DefaultJsonProtocol._

case class Showtime(startTime: DateTime, theater: Theater) {
  val fmt = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm")
  def toJson: JsObject = {
    JsObject("start-time" -> JsString(startTime.toString(fmt)), "theater" -> JsString(theater.name))
  }
}