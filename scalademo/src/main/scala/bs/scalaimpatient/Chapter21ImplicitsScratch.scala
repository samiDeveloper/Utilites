package bs.scalaimpatient

import java.io.File
import scala.io.Source

object Chapter21ImplicitsScratch extends App {
  {

    class RichFile(val from: File) {
      def read = Source.fromFile(from.getPath).mkString
    }
    implicit def file2RichFile(from: File) = new RichFile(from)
    val res = Chapter21ImplicitsScratch.getClass.getResource("/chapter21file.txt")
    val file = new File(res.getPath)
    println("file contents: " + file.read)
  }
  {
    case class Delimiters(val left: String, val right: String)
    def quote(text: String)(implicit delims: Delimiters) =
      delims.left + text + delims.right

    println(quote("asdf")(Delimiters("<<", ">>")))
    implicit val implicitDelims = Delimiters("<<<", ">>>")
    println(quote("asdf"))
  }
  {
    // implicit conversion w implicit parameters
    
    // order is an implicit param. it is also an implicit conversion so we can omit the call to order
    def smaller[T](l: T, r: T)(implicit order: T => Ordered[T]) = if (l < r) l else r

    // the below works because the Predef object defines implicit conversion for Int to Ordered[Int], how?
    println(smaller(6, 4))

    {
      // ok, now we need an Ordered for a new type
      case class MyInt(i: Int)
      implicit def myInt2Ordered: (MyInt => Ordered[MyInt]) =
        (m: MyInt) => new Ordered[MyInt] {
          override def compare(that: MyInt) = m.i - that.i
        }
      println { smaller(MyInt(7), MyInt(5)) }
    }
    {
      // or implement Comparable and apply implicit conversion in Ordered from Comparable to Ordered
      case class MyInt(i: Int) extends Comparable[MyInt] {
        override def compareTo(that: MyInt) = this.i - that.i
      }
      println { smaller(MyInt(7), MyInt(5)) }
    }
  }

}