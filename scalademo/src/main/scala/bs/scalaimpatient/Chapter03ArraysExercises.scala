package bs.scalaimpatient

import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ArrayOps
import scala.util.Sorting
import java.util.TimeZone
import java.awt.datatransfer.SystemFlavorMap
import java.awt.datatransfer.DataFlavor
import scala.collection.mutable.Buffer
import scala.collection.JavaConversions

object Chapter03Exercises extends App {

  // use arr: Seq instead of Array type to support Array and ArrayBuffer
  def printarr(arr: Seq[_ <: Any]) {
    for (e <- arr) print(e + " ")
    println
  }

  println("1. init random values array")
  val rndarr = new Array[Int](10)
  for (i <- 0 to 9) rndarr(i) = Random.nextInt(20)
  printarr(rndarr)

  println("or")
  val rndarrv2 = (for (i <- 0 to 9) yield Random.nextInt(20)).toArray
  printarr(rndarrv2)

  println("2. swap adjacent")
  val adjarr = ArrayBuffer(1, 2, 3, 4, 5)
  for (i <- 0.to((adjarr.length / 2) * 2 - 1, 2)) {
    val t = adjarr(i)
    adjarr(i) = adjarr(i + 1)
    adjarr(i + 1) = t
  }
  printarr(adjarr)

  println("3. swap to new array")
  val adjarr2 = Array(1, 2, 3, 4, 5)
  val adjarr3 = for (i <- 0.to(adjarr2.length - 1)) yield if (i < adjarr2.length - 1)
    adjarr2(i - ((i % 2) * 2 - 1)) // swap
  else adjarr2(i) // last element if odd length
  printarr(adjarr3)

  println("4. split int array")
  val tosplit = (for (i <- 0 to 9) yield Random.nextInt(20) - 10).toArray
  printarr(tosplit)
  val pos = new ArrayBuffer[Int]()
  val neg = new ArrayBuffer[Int]()
  for (e <- tosplit) if (e > 0) pos += e else neg += e
  print("pos > 0 : "); printarr(pos)
  print("neg <= 0: "); printarr(neg)

  println("5. double array avg")
  val darr = Array(2.5, 7.4, 1, 9)
  var sum: Double = 0
  darr.foreach(sum += _)
  println("avg: " + sum / darr.length)

  println("or simply")
  println("avg: " + darr.sum / darr.length)

  println("6. reverse array and...")
  val revarr = Array(3, 2, 1, 5, 4)
  Sorting.quickSort(revarr)
  val revdarr = revarr.reverse
  printarr(revdarr)

  println("...ArrayBuffer")
  val revarb = ArrayBuffer(3, 2, 1, 5, 4)
  val revdarb = revarb.sorted.reverse
  printarr(revdarb)

  println("7. duplicates")
  val duparr = Array(3, 2, 2, 7, 5, 8, 5, 3, 1)
  printarr(duparr.distinct)

  println("8. rewrite remove all but first negative")
  val negarr = ArrayBuffer(4, 9, -2, 3, -5, -8, 1, 0, -1, 9)
  val negindexes = (0 to negarr.length - 1).filter(negarr(_) < 0)
  val negindtoremove = negindexes.reverse.dropRight(1)
  for (i <- negindtoremove) negarr.remove(i)
  printarr(negarr)

  println("9. timezones")
  val timezones = TimeZone.getAvailableIDs.filter(_.startsWith("America")).map(_.stripPrefix("America/")).sorted
  print("timezones (" + timezones.length + "): ")
  printarr(timezones)

  println("10. flavors")
  val flavors = SystemFlavorMap.getDefaultFlavorMap.asInstanceOf[SystemFlavorMap]
  val imageFlav: Buffer[String] = JavaConversions.asScalaBuffer(flavors.getNativesForFlavor(DataFlavor.imageFlavor))
  println(imageFlav)
}