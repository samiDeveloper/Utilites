package bs.scalaimpatient

import scala.util.Sorting
import java.util.Scanner
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions.bufferAsJavaList;
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.Buffer

object Chapter03Scratch extends App {
  // transform (map) array: for .. yield
  println("// transform (map) array: for .. yield")
  val a = Array(2, 3, 5, 8, 11)

  val formapres = for (e <- a) yield 2 * e
  for (r <- formapres) print(r + " ")
  println

  // filter and map: for .. if .. yield
  println("// filter and map: for .. if .. yield")
  val forfiltermapres = for (e <- a if e % 2 == 0) yield 2 * e
  for (r <- forfiltermapres) print(r + " ")
  println

  // or
  println("// or")
  val filtermapres = a.filter(_ % 2 == 0).map(_ * 2)
  for (r <- filtermapres) print(r + " ")
  println

  // sort
  println("// sort")
  val strarr = Array("grmbl", "argh", "aaargh", "hhnngg")
  for (s <- strarr.sorted) print(s + " ")
  println
  for (s <- strarr.sortWith(_ > _)) print(s + " ")
  println

  // array tostring
  println("\n// array tostring")
  println(strarr.mkString(" - "))
  println(strarr.mkString("<", " and ", ">"))

  // matrix
  println("// matrix")
  val matrix = Array.ofDim[Double](2, 3)
  matrix(0)(0) = 42
  matrix(1)(2) = 54
  matrix(0)(1) = 13
  matrix.foreach { row => row.foreach(cell => print(cell + " ")); println }

  // java interop...
  println("// java interop")
  val scanner = new Scanner("6-4-8-2");
  scanner.useDelimiter("-");
  while (scanner.hasNext()) {
    print(scanner.next + " ")
  }
  println

  // auto conversion by import scala.collection.JavaConversions.bufferAsJavaList
  val command = ArrayBuffer("ls", "-ltr", "/home/bs")
  val pb = new ProcessBuilder(command); 

  // auto conversion by import scala.collection.JavaConversions.asScalaBuffer
  val cmd: Buffer[String] = pb.command(); 
  println(cmd.getClass)
  for (e <- cmd) print(e + " ")
  println
  
  
  
}