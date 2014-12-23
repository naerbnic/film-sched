package filmsched

import java.io._
import scala.io.Source
import spray.json._
import DefaultJsonProtocol._
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import scala.collection.immutable.TreeSet
import scala.collection.mutable.MultiMap
import scala.collection.mutable.HashMap
import scala.collection.{mutable => mut}
import org.joda.time.DateTime

object FormatValidator {
  val fmt = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm")
  
  val movieShowtimeMap = new HashMap[Film, mut.Set[Showtime]] with MultiMap[Film, Showtime]
  
  def readContents(f: File): String =
    Source.fromFile(f).mkString
    
  def validateStartTime(time: String): DateTime = {
    fmt.parseDateTime(time)
  }
    
  def validateShowtime(value: JsValue): Showtime = {
    val obj = value.asJsObject("Showtimes must be objects")
    
    assert(obj.fields.keySet == Set("start-time", "theater"))
    Showtime(
    validateStartTime(obj.fields("start-time").convertTo[String]),
    Theater(obj.fields("theater").convertTo[String]))
  }
    
  def validateMovie(value: JsValue) {
    val obj = value.asJsObject("Movies must be objects")
    
    // We have the right fields
    assert(obj.fields.keySet == Set("title", "length", "rating", "showtimes"),
        s"Wrong set of fields with: ${obj}")
        
    val film = Film(
        obj.fields("title").convertTo[String], 
        Duration.standardMinutes(obj.fields("length").convertTo[Int]), 
        obj.fields("rating").convertTo[Double])
        
    for (showtimeObj <- obj.fields("showtimes").convertTo[Seq[JsValue]]) {
      val showtime = validateShowtime(showtimeObj)
      movieShowtimeMap.addBinding(film, showtime)
    }
  }
  
  def validate(f: File) {
    println("Validating '" + f.toPath().toString() + "'")
    val contents = readContents(f)
    
    for (movie <- contents.parseJson.convertTo[Seq[JsValue]]) {
      validateMovie(movie)
    }
  }
    
  def main(args: Array[String]) {
    val base = new File("src/main/resources/days")
    for (file <- base.listFiles()) {
      validate(file)
    }
    
    val filmObjs = for {
      (film, showtimes) <- movieShowtimeMap.toSeq.sortBy(_._1.title)
    } yield {
      val sortedShowtimes = showtimes.toSeq.sortWith((x, y) => x.startTime.compareTo(y.startTime) > 0)
      film.toJson(sortedShowtimes)
    }
    
    val doc = JsArray(filmObjs: _*)
    import java.nio.file.{Paths, Files}
    import java.nio.charset.StandardCharsets

    Files.write(Paths.get("src/main/resources/movies2.json"), doc.prettyPrint.getBytes(StandardCharsets.UTF_8))
  }
}