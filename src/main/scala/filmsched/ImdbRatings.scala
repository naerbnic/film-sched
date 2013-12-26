package filmsched

import scala.concurrent.Future
import dispatch._
import dispatch.Defaults._
import net.liftweb.json._

object ImdbRatings {
	def ratingsForTitle(title: String): Future[Double] = {
	  val request = url("http://mymovieapi.com/") <<? Map(
	      "title" -> title,
	      "type" -> "json",
	      "plot" -> "none")
	  
	  val ratings = Http(request OK as.String) map { jsonDoc =>
	    for {
	      JArray(array) <- parse(jsonDoc)
	      JObject(movieObject) <- array
	      JField("rating", JDouble(rating)) <- movieObject
	    } yield rating
	  }
	  
	  ratings.map(_.head)
	}
	
	def ratingsForTitleNow(title: String): Double = {
	  ratingsForTitle(title)()
	}
}