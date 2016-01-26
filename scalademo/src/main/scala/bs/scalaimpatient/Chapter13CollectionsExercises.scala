package bs.scalaimpatient

object Chapter13CollectionsExercises extends App {
  // 1. map char indexes
  println("1. map char indexes")
  import scala.collection.mutable
  def indexesMut(s: String): collection.Map[Char, collection.Set[Int]] =
    s.zipWithIndex.foldLeft(mutable.Map[Char, mutable.Set[Int]]()) {
      (m, ci) => m += (ci._1 -> (m.getOrElse(ci._1, mutable.SortedSet[Int]()) + ci._2))
    }
  println { indexesMut("Mississippi") }

  // 2. map immutable
  println("2. map immutable")
  def indexesImmut(s: String): Map[Char, List[Int]] =
    s.zipWithIndex.foldLeft(Map[Char, List[Int]]()) {
      (m, ci) => m + (ci._1 -> (ci._2 :: m.getOrElse(ci._1, List[Int]())))
    }
  println { indexesImmut("Mississippi") }

  // 3. remove zeros from linkedlist
  println("3. remove zeros from linkedlist")
  def removeZeros(l: List[Int]): List[Int] = {
    if (l == Nil) Nil
    else if (l.head == 0) removeZeros(l.tail)
    else l.head :: removeZeros(l.tail)
  }
  println { removeZeros(List(5, 343, 0, -54, 0, 33, 0)) }

  // 4. filter strings
  println("4. filter strings")
  def filterStrings(strings: Iterable[String], map: Map[String, Int]) = strings.flatMap(map.get(_))
  println { filterStrings(Seq("Tom", "Fred", "Harry"), Map("Tom" -> 2, "Dick" -> 3, "Harry" -> 4)) }

  // 5. mkString
  println("5. mkString")
  def mkString(iter: Iterable[Any], start: String, sep: String, end: String): String = {
    start + iter.reduceLeft((res, a) => res + sep + a) + end
  }
  println { mkString(Array(3, 5, 7), "<<<<", "|", ">>>>") }

  // 6. foldLeft foldRight
  println("6. foldLeft foldRight")
  val foldints = 7 :: -3 :: 2 :: 9 :: Nil
  println { (foldints :\ List[Int]())(_ :: _) } // right fold to list
  println { (List[Int]() /: foldints)(_ :+ _) } // left fold to list
  println { (List[Int]() /: foldints)((list, elem) => elem +: list) } // reverse left fold, name the params to reverse em

  // 7. tupled
  println("7. tupled")
  val prices = List(5.0, 20.0, 9.95)
  val quantities = List(10, 2, 1)
  println { (prices zip quantities).map(p => p._1 * p._2) }
  println { (prices zip quantities).map(Function.tupled((l, r) => l * r)) } // tupled changes function with to args to one that takes a tuple

  // 8. grouped
  println("8. grouped")
  def matrix(values: Array[Double], numCols: Int): Array[Array[Double]] = values.grouped(numCols).toArray
  println { matrix(Array(1, 2, 3, 4, 5, 6), 3).deep }

  // 10. parallelize string computation
  println("10. parallelize string computation")
  val longstr = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat"
  val charcount = longstr.aggregate(Map[Char, Int]())(
    (m, c) => m + (c -> (m.getOrElse(c, 0) + 1)), _ ++ _)
  println(charcount)

}