package filmsched

import com.github.nscala_time.time.Imports._
import scala.math.Ordering

case class ShowtimeInstance(film: Film, showtime: Showtime) {
  lazy val endTime = showtime.startTime + film.length
  lazy val interval = new Interval(showtime.startTime, endTime)
  
  def conflicts(other: ShowtimeInstance): Boolean = {
    if (interval.overlaps(other.interval)) {
      true
    } else {
      val gapInterval = interval.gap(other.interval)
      val timeBetween = if (gapInterval == null) {
        Duration.standardMinutes(0)
      } else {
      	gapInterval.toDuration()
      }
      
      val minTimeBetween = if (showtime.theater == other.showtime.theater) {
        ShowtimeInstance.SAME_THEATER_DURATION
      } else {
        ShowtimeInstance.DIFF_THEATER_DURATION
      }
      timeBetween < minTimeBetween
    }
  }
  
  override def toString() = 
    s"'${film.title}' @ ${showtime.startTime.toString(ShowtimeInstance.printFmt)}"
}

object ShowtimeInstance {
  private val SAME_THEATER_DURATION = Duration.standardMinutes(60)
  private val DIFF_THEATER_DURATION = Duration.standardMinutes(75)
  val printFmt = DateTimeFormat.forStyle("SS")
}