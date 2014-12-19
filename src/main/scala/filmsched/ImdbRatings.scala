package filmsched

import scala.concurrent.Future
import dispatch._
import dispatch.Defaults._
import spray.json._
import DefaultJsonProtocol._

object ImdbRatings {
	def ratingsForTitle(title: String): Future[Double] = {
	  val request = url("http://mymovieapi.com/") <<? Map(
	      "title" -> title,
	      "type" -> "json",
	      "plot" -> "none")
	  
	  val ratings = Http.configure(_ setFollowRedirects true)(request OK as.String) map { jsonDoc =>
	    println(jsonDoc)
      val parsedJson = jsonDoc.parseJson
      val JsArray(objects) = parsedJson
      
      for { obj <- objects } yield {
        val Seq(ratingVal) = obj.asJsObject.getFields("rating")
        val JsNumber(rating) = ratingVal
        rating
      }
	  }
	  
	  ratings.map(_.head.doubleValue())
	}
	
	def ratingsForTitleNow(title: String): Double = {
	  ratingsForTitle(title)()
	}
}