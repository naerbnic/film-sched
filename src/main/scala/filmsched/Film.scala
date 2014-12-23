package filmsched

import com.github.nscala_time.time.Imports._
import spray.json._
import DefaultJsonProtocol._

case class Film(title: String, length: Duration, rating: Double) {
  def toJson(showtimes: Seq[Showtime]): JsObject = {
    val showtimesArray = JsArray(showtimes.map(_.toJson): _*)
    JsObject(
        "title" -> JsString(title),
        "length" -> JsNumber(length.getStandardMinutes),
        "rating" -> JsNumber(rating),
        "showtimes" -> showtimesArray)
  }
}