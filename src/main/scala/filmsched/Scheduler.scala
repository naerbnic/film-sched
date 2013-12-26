package filmsched

import scala.collection.SortedSet

object Scheduler {
  def filmScore(film: Film): Double = {
    0.0
  }
  
  class FilmRatings
  
  def findBestNext(showtimes: SortedSet[Showtime]): (Showtime, SortedSet[Showtime]) = {
    val first = showtimes.head
    val rest = showtimes.tail
    
    val (conflicts, nonConflicts) = rest.partition(first.conflicts _)
    val maxConflictScore = conflicts.map(show => filmScore(show.film)).max
    
    if (maxConflictScore > filmScore(first.film)) {
      findBestNext(rest)
    } else {
      (first, nonConflicts)
    }
  }
  
  def findBest(showtimes: SortedSet[Showtime]): SortedSet[Showtime] = {
    if (showtimes.isEmpty) {
      SortedSet()
    } else {
      val (best, rest) = findBestNext(showtimes)
      SortedSet(best) ++ findBest(rest)
    }
  }
  
  def findOverallBest(showtimes: SortedSet[Showtime]): SortedSet[Showtime] = {
    val currBest = findBest(showtimes)
    
    val remainingShows = showtimes.filterNot(filmA => currBest.exists(_ conflicts filmA))
    
    currBest ++ (if (remainingShows.isEmpty) {
      SortedSet[Showtime]()
    } else {
      findOverallBest(remainingShows)
    })
  }
  
  def greedySchedule(showtimes: Set[Showtime]): Seq[Showtime] = {
  	val orderedShowtimes = SortedSet.empty[Showtime] ++ showtimes
  	
  	null
  }
}