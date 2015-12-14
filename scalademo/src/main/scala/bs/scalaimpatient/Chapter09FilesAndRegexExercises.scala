package bs.scalaimpatient

import scala.io.Source
import java.io.PrintWriter
import java.io.File
import java.util.Scanner
import scala.collection.JavaConversions

// we read from resource instead of file
object Chapter09FilesAndRegexExercises extends App {

  val isreverse = getClass.getResourceAsStream("/chapter09.input.txt")

  // 1. reverse file lines
  println("1. reverse file lines")
  val inputlines = Source.fromInputStream(isreverse).getLines.toArray
  println(inputlines.reverse.mkString("\n"))

  // 2. replace tabs with spaces
  println("2. replace tabs with spaces")
  val istabreplace = getClass.getResourceAsStream("/chapter09.tabs.txt")
  val linesreplacetabs = Source.fromInputStream(istabreplace).getLines().toArray
  val linesfixedcols = linesreplacetabs
    .map(format(_, 15))
    .map(_ + "\n")
    .foldLeft("")(_ + _)
  println(linesfixedcols)
  val fixedcolswriter = new PrintWriter(new File("target/chapter09.out.txt"))
  fixedcolswriter.print(linesfixedcols)
  fixedcolswriter.close()

  // again, dont forget the '=' here!
  def format(tabseparated: String, colwidth: Int) = {
    tabseparated.split("\t").map(_.padTo(colwidth, " ").mkString).foldLeft("")(_ + _)
  }

  // 3. print 12+ char words  
  println("3. print 12+ char words")
  println(JavaConversions.asScalaIterator(new Scanner(getClass.getResourceAsStream("/chapter09.input.txt"))).filter(_.length >= 12).toList)

  // 4. analyze floats, ok then lets read a file for once
  println("4. analyze floats")
  val floats = Source.fromFile("target/classes/chapter09.floats.txt").getLines().flatMap(_.split("""\s""")).map(_.toFloat).toList
  println("length: " + floats.length)
  // do it yourself...
  val sum = floats.foldLeft(0.0)(_ + _)
  println("sum: " + sum)
  val avg = sum / floats.length
  println("avg: " + avg)
  val max = floats.foldLeft(0.0)((l, r) => if (l > r) l; else r)
  println("max: " + max)
  val min = floats.foldLeft(max)((l, r) => if (l < r) l; else r)
  println("min: " + min)
  // ...or go like floats.max, floats.sum

  // 5. powers of 2
  println("5. powers of 2")
  import scala.math._
  val powers = (0 to 10).map(pow(2, _).toInt).map(p => new Tuple2(p, 1.0 / p)).toList
  println("here: " + powers)
  val lines = powers.map(p => ("%8s".format(p._1) + "    " + p._2.toString))
  val formatted = lines.foldLeft("")(_ + "\n" + _)
  println(formatted)
  val pw = new PrintWriter("target/classes/chapter09.out.txt")
  pw.print(formatted)
  pw.close
  
  // 6. quoted strings
  
}