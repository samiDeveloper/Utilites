package bs.scalaimpatient

object Chapter13CollectionsScratch extends App {
  println(Iterable("a", "b", "c"))
  println(Seq("a", "b", "c"))
  println(Set("a", "b", "b", "c"))
  println(Map(("a", 1), ("b", 2)))
  println(Map("a" -> 1, "b" -> 2))

  val list = List(2, 4, 6)
  println(list.head, list.tail, list.tail.tail, list.tail.tail.tail)

  println(9 :: List(3, 4, 5))
  println(9 +: List(3, 4, 5))
  println(List(3, 4, 5) :+ 9)

  def sum(lst: List[Int]): Int = {
    if (lst == Nil) 0 else lst.head + sum(lst.tail)
  }
  println("sum: " + sum(List(4, 12, 2)))

  // reduce and left fold '/:'
  println("reduce and left fold '/:'")
  println { List(1, 6, 3).reduce(_ + _) }
  println { List(1, 6, 3).foldLeft(0)(_ + _) }

  // The /: is a left fold. It ends with ':' so it is right associative, and operates on the list. 
  // The '0' is the folds initial element. 
  // The /: is curried. The last param is the binary reduce function
  println { (0 /: List(1, 6, 3))(_ + _) }

  // charachter count, using more complex reduce
  println("more complex reduce: charachter count")
  println {
    "Mississippi".foldLeft(Map[Char, Int]())(
      (m, c) => m + (c -> (m.getOrElse(c, 0) + 1)))
  }
  // or
  println {
    (Map[Char, Int]() /: "Mississippi") {
      (m, c) => m + (c -> (m.getOrElse(c, 0) + 1))
    }
  }

  // scanLeft combines folding and mapping
  println("scanLeft")
  println { (1 to 10).scanLeft(0)(_ + _) }

  // zip
  println("zip")
  println("Scala".zipWithIndex.max)
  println("Scala".zipWithIndex.max._2)

  // collection -- lazy --> iterator -- immutable --> stream: immutable list tail is computed lazily

  // streams, caching 
  println("streams")
  def numsFrom(n: BigInt): Stream[BigInt] = n #:: numsFrom(n + 1)
  val tenOrMore = numsFrom(10)
  println("tenOrMore: " + tenOrMore)
  println(tenOrMore.head)
  println("tenOrMore: " + tenOrMore)
  val elevenOrMore = tenOrMore.tail
  println("tenOrMore: " + tenOrMore)
  println(elevenOrMore)
  println(tenOrMore.tail.tail.tail)
  println("tenOrMore: " + tenOrMore)
  println("tenOrMore(2): " + tenOrMore(2))
  println("tenOrMore(5): " + tenOrMore(5))

  println { "Range toStream: " + (10 to 20).toStream.tail.tail.tail }
  val tenTo20ToStream = (10 to 20).toStream
  tenTo20ToStream.tail.tail.tail
  println { "Range toStream: " + tenTo20ToStream }

  def fibFrom(a: Int, b: Int): Stream[Int] = a #:: fibFrom(b, a + b)
  print("fibFrom(0, 1): "); fibFrom(0, 1).take(7).foreach((i: Int) => print(i + " "))
  println

  // lazy view, no caching
  import scala.math._
  println("lazy view")
  val powersEager = (1 to 5).map(i => { print("eag pass: " + i + ", "); i }).map(pow(10, _))
  val powersLazy = (1 to 5).view.map(i => { print("lzy pass: " + i + ", "); i }).map(pow(10, _))
  println
  println("powerEager 3 = " + powersEager(3))
  println("powerLazy 3 = " + powersLazy(3)) // computes only the 4th element, no caching, avoids building large intermediate collections

  println { "eagerPowerRecip: " + (1 to 5).map(pow(10, _)).map(1 / _) } // same result as below, but...
  println { "lazyPowerRecip:  " + (1 to 5).view.map(pow(10, _)).map(1 / _).force } // guess this behaviour is more like Java Streams, no intermediate collections

  // parallel
  println("parallel")
  for (i <- (0 to 100)) print(i + " ")
  println
  for (i <- (0 to 100).par) print(i + " ")

}