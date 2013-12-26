package filmsched

import com.github.nscala_time.time.Imports._
import scala.math.Ordering

case class Showtime(film: Film, startTime: DateTime, theater: Theater) {
  lazy val endTime = startTime + film.length
  lazy val interval = new Interval()
  def conflicts(other: Showtime): Boolean = {
    if (interval.overlaps(other.interval)) {
      false
    } else {
      val timeBetween = interval.gap(other.interval).toDuration()
      val minTimeBetween = if (theater == other.theater) {
        Showtime.SAME_THEATER_PERIOD
      } else {
        Showtime.DIFF_THEATER_PERIOD
      }
      minTimeBetween <= timeBetween
    }
  }
}

object Showtime {
  val SAME_THEATER_PERIOD = Duration.standardMinutes(60)
  val DIFF_THEATER_PERIOD = Duration.standardMinutes(75)
  
  implicit val ShowtimeOrdering: Ordering[Showtime] =
    Ordering.by{x: Showtime => x.startTime.getMillis()}
}