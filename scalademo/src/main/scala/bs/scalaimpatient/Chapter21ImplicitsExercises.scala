package bs.scalaimpatient

import scala.io.StdIn

object Chapter21ImplicitsExercises extends App {
  // 1. how does -> work? 
  println("1. how does -> work? ")
  // Predef defines implicit class ArrowAssoc
  // -> triggers instanciation of a new ArrowAssoc passing the first (left) param
  // then invokes -> method in the instance passing the second param which results in a Tuple2

  // 2. define +% operator
  println("2. define +% operator")
  implicit final class AddPerc(private val self: Int) {
    def %+(perc: Int): Double = (self + self * perc / 100)
  }
  println { 120 %+ 10 }

  // 3. factorial operator
  println("3. factorial operator")
  implicit final class Fact(private val self: Int) {
    def ! : Int = { var r = 1; for (i <- 1 to self) r = r * i; r; }
  }
  println { 5 ! }
  
  // [aborted] 4. fluent reader, stuck w the 'does not take parameters' error message
}