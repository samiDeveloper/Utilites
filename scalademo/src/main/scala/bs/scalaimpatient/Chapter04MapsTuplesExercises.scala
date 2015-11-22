package bs.scalaimpatient

import scala.collection.immutable.Map
import java.util.Scanner
import scala.util.Sorting
import scala.collection.SortedMap
import java.util.Calendar
import scala.collection.mutable.LinkedHashMap

object Chapter04MapsTuplesExercises extends App {
  println("1. prices discount map")
  val giz = Map("hng" -> 23.95, "grmbl" -> 5.95)
  val gizdisc1 = for ((g, p) <- giz) yield (g, p * 0.9)
  println(gizdisc1)
  println("or:")
  val gizdisc2 = giz.map(e => (e._1, e._2 * 0.9))
  println(gizdisc2)

  // 2. count words, use resource instead of file
  println("2. count words")
  val scanner = new Scanner(Chapter04MapsTuplesExercises.getClass.getResourceAsStream("/chapter04.count.words.txt"))
  val wordCounts: collection.mutable.Map[String, Int] = collection.mutable.Map()
  while (scanner.hasNext()) {
    val word = scanner.next
    wordCounts.update(word, wordCounts.getOrElse(word, 0) + 1)
  }
  val wcseq: Seq[Tuple2[String, Int]] = wordCounts.toSeq
  val wcsorted = wcseq.sortBy(e => e._2).reverse
  println(wcsorted)

  // 3. count words immutable
  println("3. count words immutable")
  val scanimm = new Scanner(Chapter04MapsTuplesExercises.getClass.getResourceAsStream("/chapter04.count.words.txt"))
  var wcimm: Map[String, Int] = Map().withDefaultValue(0)
  while (scanimm.hasNext()) {
    val word = scanimm.next
    wcimm = wcimm - word + (word -> (wcimm(word) + 1))
  }
  val wcimmseq = wcimm.toSeq
  val wcimmsorted = wcimmseq.sortBy(e => e._2).reverse
  println(wcimmsorted)

  // 4. sorted map
  println("4. sorted map")
  val scansorted = new Scanner(Chapter04MapsTuplesExercises.getClass.getResourceAsStream("/chapter04.count.words.txt"))
  var wcsortedmap = collection.immutable.SortedMap[String, Int]().withDefaultValue(0)
  while (scansorted.hasNext) {
    val word = scansorted.next
    wcsortedmap = wcsortedmap - word + (word -> (wcsortedmap(word) + 1))
  }
  println(wcsortedmap)

  // 5. treemap
  println("5. treemap")
  val scantree = new Scanner(Chapter04MapsTuplesExercises.getClass.getResourceAsStream("/chapter04.count.words.txt"))
  val wctreemap = new java.util.TreeMap[String, Int] // by default sorts keys in natural order
  while (scantree hasNext) {
    val word = scantree.next
    val countopt = Option(wctreemap.get(word))
    wctreemap.put(word, countopt.getOrElse(0) + 1)
  }
  println(wctreemap)

  // 6. weekdays linked hashmap
  println("6. weekdays linked hashmap")
  val weekdays = LinkedHashMap("Monday" -> Calendar.MONDAY, "Tuesday" -> Calendar.TUESDAY, "Wednesday" -> Calendar.WEDNESDAY)
  weekdays.foreach(p => print(p + " "))
  println
  println(weekdays)

  // 7. print formatted properties
  println("7. print formatted properties")
  val sysprops = collection.JavaConversions.propertiesAsScalaMap(System.getProperties)
  // val maxkeylen = sysprops.keySet.max(Ordering.by[String, Int](k => k.length)).length
  // ah, that's better: 
  val maxkeylen = sysprops.keySet.maxBy(_.length).length
  for (e <- sysprops) println(e._1 + (" " * (maxkeylen + 5 - e._1.length)) + "| " + e._2)

  // 8. minmax
  println("8. minmax")
  def minmax(ints: Array[Int]) = {
    (ints.min, ints.max)
  }
  println(minmax(Array(4, 2, 9, 6, 7)))

  // 9. lteqgt
  println("9. lteqgt")
  def lteqgt(values: Array[Int], v: Int) = {
    (values.count(_ < v), values.count(_ == v), values.count(_ > v))
  }
  println(lteqgt(Array(4, 2, 9, 6, 7), 4))

  // 10. zip
  println("10. zip")
  println("Hello".zip("World"))
  // I've build my own crypto: <https://twitter.com/old_sound/status/602996592531091456>
}