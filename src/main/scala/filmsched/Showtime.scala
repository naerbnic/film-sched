package filmsched

import com.github.nscala_time.time.Imports._
import scala.math.Ordering

case class Showtime(film: Film, startTime: DateTime, theater: Theater) {
  lazy val endTime = startTime + film.length
  lazy val interval = new Interval(startTime, endTime)
  def innerConflicts(other: Showtime): Boolean = {
    if (interval.overlaps(other.interval)) {
      true
    } else {
      val gapInterval = interval.gap(other.interval)
      if (gapInterval == null) {
        true
      } else {
      	val timeBetween = gapInterval.toDuration()
	      val minTimeBetween = if (theater == other.theater) {
	        Showtime.SAME_THEATER_PERIOD
	      } else {
	        Showtime.DIFF_THEATER_PERIOD
	      }
	      timeBetween < minTimeBetween
      }
    }
  }
  
  def conflicts (other: Showtime): Boolean = {
    val result = innerConflicts(other)
    println(s"$this vs $other = $result")
    result
  }
  
  require (this conflicts this)
}

object Showtime {
  val SAME_THEATER_PERIOD = Duration.standardMinutes(60)
  val DIFF_THEATER_PERIOD = Duration.standardMinutes(75)
}