package bs.scalaimpatient

object Chapter17TypeParametersExercises extends App {
  // 1. immutable pair
  println("1. immutable pair")

  {
    case class Pair[T](first: T, second: T) {
      def swap() = { Pair(second, first) }
    }
    val p = Pair("a", "b")
    println("p1     : " + p)
    println("swapped: " + p.swap)
  }

  // 2. mutable pair
  println("2. mutable pair")

  {
    class Pair[T](var first: T, var second: T) {
      def swap() {
        val t = first
        first = second
        second = t
      }
      override def toString = "Pair(" + first + "," + second + ")"
    }
    val p = new Pair("a", "b")
    println("p: " + p)
    println("swap...")
    p.swap
    println("p: " + p)
  }

  // 3. swap pair[T,S] 
  println("3. swap pair[T,S]")

  {
    case class Pair[T, S](first: T, second: S)
    def swap[T, S](p: Pair[T, S]) = Pair(p.second, p.first)
    val p1 = Pair(1, "a")
    println("p1: " + p1)
    println("p2: " + swap(p1))
  }

  // 6. middle
  println("6. middle")
  def middle[T](it: Iterable[T]): T = {
    it.drop((it.size - 1) / 2).head
  }
  println { middle("helbo") }

  // 7 iterable[+A] def find(p: (A) ⇒ Boolean): Option[A]  - the A is a result (the Iterable acts as a producer) ==> covariant

  // 8. type bounds on mutable type
  println("8. type bounds on mutable type")

  {
    class Pair[T](var first: T, var second: T) {
      override def toString = "Pair(" + first + "," + second + ")"

      // error because R does not match the type T of the first var
      //      def replaceFirst[R >: T](newFirst: R) { first = newFirst }
    }
  }

  // 9. X covariant unsound override
  println("9. X covariant unsound override")
  import scala.math.sqrt
  import scala.Double

  {
    class Pair[+T](first: T, second: T) {
      // pair is immutable, yet compiler error if we omit the [S >: T] because newFirst is a param and not a result
      def replaceFirst[S >: T](newFirst: S) = new Pair(newFirst, second)
    }

    class NastyDoublePair(first: Double, second: Double) extends Pair[Double](first, second) {
      //      override def replaceFirst[Double](newFirst: scala.Double)=super.replaceFirst(sqrt(newFirst)) // will not compile
    }
  }

  // 10. mutable pair swap
  println("10. mutable pair swap")

  {
    class Pair[S, T](var first: S, var second: T) {
      def swap(implicit ev1: S =:= T, ev2: T =:= S) {
        val temp = first
        first = second
        second = temp
      }
      override def toString = "Pair(" + first + "," + second + ")"
    }

    val p = new Pair(1, 2)
    println(p)
    p.swap
    println(p)
  }

}