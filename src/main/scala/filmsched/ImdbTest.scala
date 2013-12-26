package filmsched

import net.liftweb.json._

object ImdbTest {
	def main(args: Array[String]) {
	  println(ImdbRatings.ratingsForTitleNow("\"The Dog\""))
	}
}