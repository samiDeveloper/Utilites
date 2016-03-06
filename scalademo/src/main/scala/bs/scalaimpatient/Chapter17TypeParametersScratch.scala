package bs.scalaimpatient

import java.net.URL

object Chapter17TypeParametersScratch extends App {

  {
    // <: upperBound
    class Pair[T <: Comparable[T]](val first: T, val second: T) {
      def smaller = if (first.compareTo(second) < 0) first else second
    }
    val p1 = new Pair[String]("a", "b")
    //    val p2 = new Pair[Int](1, 2) // wont compile
  }

  {
    // <% ViewBound, takes implicit conversion RichInt implementing Comparable
    class Pair[T <% Comparable[T]](val first: T, val second: T) {
      def smaller = if (first.compareTo(second) < 0) first else second
    }
    val p2 = new Pair[Int](1, 2) // now it will
  }

  {
    // >: lowerBound 
    class Pair[T](val first: T, val second: T) {
      def replaceFirst[R >: T](newFirst: R) = new Pair[R](newFirst, second)
    }
  }

  {
    // : ContextBound, implicit value is more flexible than implicit conversion, but takes parameter
    class Pair[T: Ordering](val first: T, val second: T) {
      def smaller(implicit ord: Ordering[T]) = if (ord.compare(first, second) < 0) first else second
    }
  }

  {
    // : Manifest, Manifest ContextBound, instantiate generic Array
    def makePair[T: Manifest](first: T, second: T) = {
      val r = new Array[T](2); r(0) = first; r(1) = second; r
    }
    val p: Array[Int] = makePair(5, 6)
  }

  {
    // =:=, <:< and <%<, Type Constraints, 'implicit evidence parameter' enables use '<' operator on T
    class Pair[T](first: T, second: T) {
      def smaller(implicit evidence: T <:< Ordered[T]) =
        if (first < second) first else second
    }
    val p = new Pair[URL](new URL("http://c.d"), new URL("http://t.s")) // URL is not Ordered, but we can make a Pair[URL]
    //    p.smaller // can make Pair[URL] but not invoke smaller

    val bigdecopt: Option[java.math.BigDecimal] = Option(new java.math.BigDecimal("1.11"))
    bigdecopt.orNull // orNull has a Null <:< A1 Type Constraint (Null is a subtype of all reference types)

    val intopt: Option[Int] = Option(1)
    //    nopt.orNull // but it wont work for Int because primitive int cant be null

    // improve type inference
    def firstLast1[A, C <: Iterable[A]](it: C) = (it.head, it.tail)
    //        firstLast1(List(1,2,3)) // type inferencer cannot figure out A

    def firstLast2[A, C](it: C)(implicit ev: C <:< Iterable[A]) = (it.head, it.tail)
    firstLast2(List(1, 2, 3))
  }

  {
    // Covariance
    class Person
    case class Student() extends Person

    {
      case class Pair[T](first: T, second: T)
      def makeFriends(p: Pair[Person]) {}
      makeFriends(Pair(new Person(), new Person()))
      //        makeFriends(Pair(Student(), Student())) // cant, no relation between Pair[Student] and required Pair[Person]
    }
    {
      case class Pair[+T](first: T, second: T) // +T covariance same direction, a Pair[Student] is now subtype of Pair[Person]
      def makeFriends(p: Pair[Person]) {} 
      makeFriends(Pair(Student(), Student()))
    }

    // wildcards, no need for using type variance, but you can use them
    def process(people: java.util.List[_ <: Person]){}
    process(java.util.Arrays.asList(Student()))
  }
}