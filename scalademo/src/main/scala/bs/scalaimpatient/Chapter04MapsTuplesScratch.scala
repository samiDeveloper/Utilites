package bs.scalaimpatient

import scala.collection.mutable.LinkedHashMap
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.propertiesAsScalaMap

object Chapter04MapsTuplesScratch extends App {
  val m = Map("Alice" -> 10, "Bob" -> 3, "Cindy" -> 8)
  println(m)

  println(m("Bob"))
  println(m.contains("asdf"))
  println(m contains "Alice")
  println(m getOrElse ("asdf", 0))
  println(m.getOrElse("Bob", 0))
  println(m.get("asfd"))

  val mutmap = scala.collection.mutable.Map("Alice" -> 10, "Bob" -> 3, "Cindy" -> 8)
  mutmap("Bob") = 11
  println(mutmap("Bob"))
  mutmap += ("Bob" -> 12)
  println(mutmap("Bob"))
  mutmap -= "Alice"
  println(mutmap.size)

  val m2 = m + ("Bob" -> 11)
  println(m2)
  for ((k, v) <- m2) println("key: " + k + ", value: " + v)

  // swap key value
  val swapped = for ((k, v) <- m2) yield (v, k)
  println("swapped: " + swapped)

  // create map from tuples
  val tup2 = ("asdf", 10)
  println(tup2.getClass)
  val mapfromtup = Map(tup2)
  println(mapfromtup)

  // linkedhashmap ordered keys
  println("linkedhashmap")
  val orderedmap = LinkedHashMap("Jan" -> 1, "Feb" -> 2, "Mar" -> 3)
  orderedmap.foreach(e => println("entry (" + e.getClass + "): " + e))
  orderedmap.foreach(e => println("entry key: " + e._1 + ", value: " + e._2))

  val fromjava: collection.mutable.Map[String, Int] = new java.util.TreeMap[String, Int]
  val props: collection.Map[String, String] = System.getProperties
  println("sysprops " + props.size + "(): " + props.head + ", " + props.iterator.drop(1).next() + ", ...")

  // tuples get values...
  val tup3 = ("asdf", 3, 4.23)
  println("get tuple value three: " + tup3._3)

  // or use pattern match
  val (_, _, third) = tup3
  println("tup third value: " + third)

  val symbols = Array("<", "-", ">")
  val counts = Array(2, 10, 2)
  val pairs: Seq[Tuple2[String, Int]] = symbols.zip(counts)
  for ((s, c) <- pairs) print(s * c)
}