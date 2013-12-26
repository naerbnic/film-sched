package filmsched

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import net.liftweb.json._
import com.github.nscala_time.time.Imports._
import org.joda.time.format.DateTimeFormatter

object TestSchedule {
  implicit val Formats = DefaultFormats
  val fmt = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm")
	def main(args: Array[String]) {
	   val contents = new String(Files.readAllBytes(Paths.get("src/main/resources/schedule.json")), "UTF-8")
	   val json = parse(contents)
	   
	   val value = for {
	     movie <- json.children
	   } yield {
	     val title = (movie \ "title").extract[String]
	     val length = Duration.standardMinutes((movie \ "length").extract[Int].toLong)
	     val rating = (movie \ "rating").extract[Int]
	     val film = Film(title, length, rating)
	     
	     val showtimes = for {
	       showtime <- (movie \ "showtimes").children
	     } yield {
	       val startTime = fmt.parseDateTime((showtime \ "start-time").extract[String])
	       val theater = Theater((showtime \ "theater").extract[String])
	       Showtime(film = film, startTime = startTime, theater = theater)
	     }
	     
	     showtimes
	   }
	   
	   println(Scheduler.greedySchedule(value.flatten.toSet))
	}
}