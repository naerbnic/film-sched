package filmsched

import com.github.nscala_time.time.Imports._
import scala.math.Ordering

case class Showtime(film: Film, startTime: DateTime, theater: Theater) {
  lazy val endTime = startTime + film.length
  lazy val interval = new Interval(startTime, endTime)
  
  def conflicts(other: Showtime): Boolean = {
    if (interval.overlaps(other.interval)) {
      true
    } else {
      val gapInterval = interval.gap(other.interval)
      val timeBetween = if (gapInterval == null) {
        Duration.standardMinutes(0)
      } else {
      	gapInterval.toDuration()
      }
      
      val minTimeBetween = if (theater == other.theater) {
        Showtime.SAME_THEATER_DURATION
      } else {
        Showtime.DIFF_THEATER_DURATION
      }
      timeBetween < minTimeBetween
    }
  }
  
  override def toString() =
    s"Showtime('${film.title}' @ ${startTime.toString(Showtime.printFmt)})"
}

object Showtime {
  private val SAME_THEATER_DURATION = Duration.standardMinutes(60)
  private val DIFF_THEATER_DURATION = Duration.standardMinutes(75)
  val printFmt = DateTimeFormat.forStyle("SS")
}