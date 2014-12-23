package filmsched

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeFormatter
import spray.json._
import DefaultJsonProtocol._

object TestSchedule {
  val fmt = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm")
	def main(args: Array[String]) {
	   val contents = new String(Files.readAllBytes(Paths.get("src/main/resources/movies2.json")), "UTF-8")
     val JsArray(movies) = contents.parseJson
	   
	   val value = for {
	     movie <- contents.parseJson.convertTo[Seq[JsValue]]
	   } yield {
       val Seq(titleVal, lengthVal, ratingVal, showtimesVal) =
         movie.asJsObject.getFields("title", "length", "rating", "showtimes")
	     val title = titleVal.convertTo[String]
	     val length = Duration.standardMinutes(lengthVal.convertTo[Long])
	     val rating = ratingVal.convertTo[Double]
	     val film = Film(title, length, rating)
	     
	     val showtimes = for {
	       showtime <- showtimesVal.convertTo[Seq[JsValue]]
	     } yield {
         val Seq(showtimeVal, theaterVal) = showtime.asJsObject.getFields("start-time", "theater")
	       val startTime = fmt.parseDateTime(showtimeVal.convertTo[String])
	       val theater = Theater(theaterVal.convertTo[String])
	       ShowtimeInstance(film = film, showtime = Showtime(startTime = startTime, theater = theater))
	     }
	     
	     showtimes
	   }
	   
	   println(new ConstraintScheduler().schedule(value.flatten.toSet))
	}
}