package filmsched

import scala.collection.SortedSet

object Scheduler {
  def filmScore(film: Film): Double = {
    film.rating
  }
  
  def findBestNext(showtimes: Seq[ShowtimeInstance]): (ShowtimeInstance, Seq[ShowtimeInstance]) = {
    val first = showtimes.head
    val rest = showtimes.tail
    
    val (conflicts, nonConflicts) = rest.partition(first.conflicts _)
    if (conflicts.isEmpty) {
      (first, nonConflicts)
    } else {
	    val maxConflictScore = conflicts.map(show => filmScore(show.film)).max
	    
	    if (maxConflictScore > filmScore(first.film)) {
	      findBestNext(rest)
	    } else {
	      (first, nonConflicts)
	    }
    }
  }
  
  def findBest(showtimes: Seq[ShowtimeInstance]): Seq[ShowtimeInstance] = {
    if (showtimes.isEmpty) {
      Seq()
    } else {
      val (best, rest) = findBestNext(showtimes)
      Seq(best) ++ findBest(rest)
    }
  }
  
  def findOverallBest(showtimes: Seq[ShowtimeInstance]): Seq[ShowtimeInstance] = {
    val currBest = findBest(showtimes)
    
    require(currBest.nonEmpty)
    
    val remainingShows = showtimes.filterNot(filmA => currBest.exists(_ conflicts filmA))
    
    currBest ++ (if (remainingShows.isEmpty) {
      Seq()
    } else {
      findOverallBest(remainingShows)
    })
  }
  
  def greedySchedule(showtimes: Set[ShowtimeInstance]): Seq[ShowtimeInstance] = {
  	val orderedShowtimes = showtimes.toSeq.sortBy(_.showtime.startTime.getMillis())
  	
  	findOverallBest(orderedShowtimes).toSeq
  }
}