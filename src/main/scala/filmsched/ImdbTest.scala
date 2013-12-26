package filmsched

import net.liftweb.json._

object ImdbTest {
  val test = """[1, 2 [] 3,]"""
	def main(args: Array[String]) {
	  println(parse(test))
	}
}