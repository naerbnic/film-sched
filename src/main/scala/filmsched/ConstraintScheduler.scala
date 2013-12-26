package filmsched

import JaCoP.core._
import scala.collection.{mutable => mut}
import JaCoP.constraints._
import JaCoP.search.DepthFirstSearch
import JaCoP.search.SimpleSelect
import JaCoP.search.SmallestDomain
import JaCoP.search.IndomainMin

class ConstraintScheduler {
	val store = new Store()
	val showtimeVars = mut.Map[Showtime, BooleanVar]()
	val allVars = mut.Set[IntVar]()
	
	def addShowtime(showtime: Showtime) {
	  val showtimeVar = new BooleanVar(store, s"$showtime")
	  allVars += showtimeVar
	  for ((otherShowtime, otherVar) <- showtimeVars) {
	    if (showtime conflicts otherShowtime) {
	      val bothSeen = new BooleanVar(store)
	      allVars += bothSeen
	      store.impose(new AndBool(Array[IntVar](showtimeVar, otherVar), bothSeen))
	      store.impose(new XeqC(bothSeen, 0));
	    }
	  }
	  showtimeVars.put(showtime, showtimeVar)
	}
	
	def createCost(): IntVar = {
	  val costVars = for {
	    (showtime, seen) <- showtimeVars
	  } yield {
	    val showtimeCost = new IntVar(store, s"$showtime-cost", -10, 10)
	    allVars += showtimeCost
	    store.impose(new IfThenElse(
	        new XeqC(seen, 1),
	        new XeqC(showtimeCost, -showtime.film.rating.toInt),
	        new XeqC(showtimeCost, 0)))
	    showtimeCost
	  }
	  
	  val total = new IntVar(store, "total", -10000000, 0)
	  allVars += total
	  store.impose(new Sum(costVars.toArray, total))
	  total
	}
	
	def schedule(showtimes: Set[Showtime]) {
	  showtimes.foreach(addShowtime _)
	  val cost = createCost()
	  
	  val search = new DepthFirstSearch[IntVar]()
	  val select = new SimpleSelect(allVars.toArray,
	      new SmallestDomain[IntVar](), 
        new IndomainMin[IntVar]())
	  if (search.labeling(store, select, cost)) {
	    for ( (showtime, seen) <- showtimeVars ) {
	      if (seen.value() != 0) {
	        println(showtime)
	      }
	    }
	  } else {
	    println("FAILED!")
	  }
	}
}