package bs.scalaimpatient

import scala.io.Source
import java.io.File

object Chapter09FilesAndRegex extends App {
  // use resource instead of file
  val is = getClass.getResourceAsStream("/chapter09.input.txt")
  val scratchtxt = Source.fromInputStream(is).mkString
  println(scratchtxt)
  println((scratchtxt.split("\\s+").length))

  // list subdirectories recursively
  println("user tmp dirs")
  def subdirs(dir: File): Iterator[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.flatMap(subdirs _)
  }

  val usertmp = new File(System.getProperty("user.home") + "/tmp")
  println(subdirs(usertmp).map(_.toString).mkString(", "))

  // serializable
  @SerialVersionUID(42L) class TestSerializable extends Serializable
  class TestSerializableDefaultId extends Serializable

  // process...
  println("process")
  import sys.process._

  // ! executes the ProcessBuilder, which is implicitly imported from sys.process package
  "ls -al" !

  // #> redirects, ! returns process result code
  val result = "ls -ltr" #> new File("target/out.txt") !

  // we need an empty line after the process statements?
  println("result: " + result)

  // #| pipe
  "cat target/classes/chapter09.input.txt" #| "sort" !

  // regex...
  println
  println("regex")

  val regis = getClass.getResourceAsStream("/chapter09.input.txt")
  val reginput = Source.fromInputStream(regis).mkString

  val twoLetterPattern = """\s[a-z][a-z]\s""".r // """ raw string syntax no escapes, r: regex
  val twoLetterCount = twoLetterPattern.findAllIn(reginput).length
  println("two letter word count: " + twoLetterCount)
  
  val prefix1 = "Overdag".r.findPrefixMatchOf(reginput) // returns option
  println("prefix1: " + prefix1)
  val prefix2 = "No way".r.findPrefixMatchOf(reginput) // returns option
  println("prefix2: " + prefix2)
  
  // groups
  val gradenPattern="([0-9]+) graden".r
  val allGraden = for(gradenPattern(gr) <- gradenPattern.findAllIn(reginput)) yield gr
  println("All graden: " + allGraden.mkString(", "))
}