package bs.scalaimpatient

import scala.collection.mutable.ArrayBuffer
import java.io.File
import scala.util.matching.Regex

object Chapter11OperatorsExercises extends App {
  // 1. operator precedence and associativity
  println("1. operator precedence and associativity")
  println(2 + 4 -> 5) // + and -> have same precedence so left associativity counts: (2 + 4) -> 5
  // val v = 3 -> 4 + 5 // doesn't compile because its: (3 -> 4) + 5 which falls back to String concatenation for the + operator
  // println(v.getClass + "(" + v._1.getClass + "," + v._2.getClass + "): " + v)

  // 2. BigInt pow
  println("2. BigInt pow")
  // No ** or ^ operator for pow perhaps because both typically have another meaning?  We could do it like:
  class ExtBigInt(val base: BigInt) {
    def **(exp: Int) = {
      base.pow(exp)
    }
  }
  println("2 ** 3: " + (new ExtBigInt(2) ** 3))

  // 3. Fraction
  println("3. Fraction")
  import scala.math._
  class Fraction(n: Int, d: Int) {
    private val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d)
    private val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d)
    override def toString = num + "/" + den
    def sign(a: Int) = if (a > 0) 1 else if (a < 0) -1 else 0
    def gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)
    def +(that: Fraction): Fraction = new Fraction(num * that.den + that.num * den, den * that.den)
    def -(that: Fraction): Fraction = new Fraction(num * that.den - that.num * den, den * that.den)
    def *(that: Fraction): Fraction = new Fraction(num * that.num, den * that.den)
    def /(that: Fraction): Fraction = new Fraction(num * that.den, den * that.num)
  }
  val f1 = new Fraction(2, 3)
  val f2 = new Fraction(1, 2)
  println(f1 + " + " + f2 + " = " + (f1 + f2))
  println(f2 + " + " + f1 + " = " + (f2 - f1))
  println(f1 + " * " + f2 + " = " + (f1 * f2))
  println(f1 + " / " + f2 + " = " + (f1 / f2))

  // 4. Money
  println("4. Money")
  class Money(val dollars: Int, val cents: Int) {
    def +(that: Money): Money = new Money((100 * (dollars + that.dollars) + cents + that.cents) / 100, (100 * (dollars + that.dollars) + cents + that.cents) % 100)
    def -(that: Money): Money = new Money((100 * (dollars - that.dollars) + cents - that.cents) / 100, (100 * (dollars - that.dollars) + cents - that.cents) % 100)
    def ==(that: Money): Boolean = (100 * dollars + cents) == (100 * that.dollars + that.cents)
    def <(that: Money): Boolean = (100 * dollars + cents) < (100 * that.dollars + that.cents)
    override def toString = dollars + "." + cents
  }
  val m1 = new Money(7, 75)
  val m2 = new Money(3, 50)
  val m3 = new Money(1, 80)
  val m4 = new Money(1, 80)
  println(m1 + " + " + m2 + " = " + (m1 + m2))
  println(m1 + " - " + m3 + " = " + (m1 - m3))
  println(m1 + " == " + m2 + " = " + (m1 == m2))
  println(m3 + " == " + m4 + " = " + (m3 == m4))
  println(m2 + " < " + m1 + " = " + (m2 < m1))
  println(m3 + " < " + m4 + " = " + (m3 < m4))

  // supply * and / (allocate) see http://martinfowler.com/eaaCatalog/money.html

  // 5. HTML Table
  println("5. HTML Table")
  class Table {
    //    private var row: ArrayBuffer[String] = ArrayBuffer();
    //    val rows: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer(row)
    //    def |(value: String) = row += value
    //    def ||(value: String) = { row = ArrayBuffer(); rows.append(row); |(value) }
    private val html = new StringBuilder("<html><tr>")
    def |(cellval: String) = { html.append("<td>").append(cellval).append("</td>"); this }
    def ||(cellval: String) = { html.append("</tr><tr>"); |(cellval); this }
    override def toString() = html.append("</tr></table>").toString
  }
  object Table {
    def apply() = new Table()
  }

  val html = Table() | "Java" | "Scala" || "Gosling" | "Odersky" || "JVM" | "JVM, .NET"
  println(html)

  // 6. ASCIIArt
  println("6. ASCIIArt")
  object AsciiArt {
    private val bears =
      "    ()_()()_()   \n" +
        "    / ..)(.. \\   \n" +
        " __/  ( || )  \\_ \n" +
        "(_/    *  *   (_)\n" +
        " |       |   | ||\n" +
        " |  |    |   \\_/|\n" +
        " \\__/    |      |\n" +
        "   >      \\    / \n" +
        "  <__,--,__|/|_> \n" +
        "                 \n" +
        "                 \n"

    private val bat =
      "    ____.                            .____      \n" +
        "  ..'     ''\\                      /''     '..  \n" +
        ".'     .. . .'\\.                ./. .  ..     '.\n" +
        " ')__.'  ...    '.)          (.'    ...  '.__(' \n" +
        "  '  )_.'     .   :          :   .     '._(  '  \n" +
        "       )_.__.'    '.        .'    '.__._(       \n" +
        "            )__.'   '.    .'   '.__(         ARV\n" +
        "            '   \\._..:^::^:.._./   '            \n" +
        "                 '   :o..o:   '                 \n" +
        "                    .:'!!':.                    \n" +
        "                    ''    ''                    \n"

    def mkBears() = new AsciiArt(bears)
    def mkBat() = new AsciiArt(bat)
  }

  class AsciiArt(private val drawing: String) {
    /** combine horizontally */
    def +(thatdrawing: AsciiArt): AsciiArt = {
      val combined = drawing.split("\n").zip(thatdrawing.drawing.split("\n")).map(s => s._1 + "   " + s._2).mkString("\n")
      new AsciiArt(combined)
    }
    /** combine vertically */
    def |(thatdrawing: AsciiArt): AsciiArt = {
      new AsciiArt(drawing + "\n" + thatdrawing.drawing)
    }
    override def toString() = drawing
  }

  println(AsciiArt.mkBears + AsciiArt.mkBat | AsciiArt.mkBat + AsciiArt.mkBears)

  // 7. BitSequence
  println("7. BitSequence")
  class BitSequence(private var bits: Long = 0) {
    // sets a bit
    def update(bit: Short, value: Boolean) {
      if (value) bits |= 1 << bit
      else bits &= ~(1 << bit)
    }
    // gets a bit
    def apply(bit: Short): Boolean = (bits & pow(2, bit).toLong) > 0

    override def toString() = bits.toBinaryString
  }

  // call update...
  val bs1 = new BitSequence(15)
  println(bs1) // 1111

  bs1(2) = false // equivalent to bs1.update(2, false) 
  println(bs1) // 1011

  bs1(5) = true
  println(bs1) // 101011

  bs1(2) = true
  println(bs1) // 101111

  // call apply...
  println("bit 0: " + bs1(0))
  println("bit 1: " + bs1(1))
  println("bit 2: " + bs1(2))
  println("bit 3: " + bs1(3))
  println("bit 4: " + bs1(4))
  println("bit 5: " + bs1(5))

  // 8. The Matrix
  println("8. The Matrix")
  val testSeq = 1 :: 3 :: 2 :: Nil
  testSeq.toArray

  class Matrix(private val rows: Array[Array[Int]] = Array.ofDim[Int](2, 2)) {
    def update(row: Byte, col: Byte, value: Int) { rows(row)(col) = value }
    def transpose() = new Matrix((for (i <- 0 to rows(0).length - 1) yield rows.map(_(i)).toArray).toArray)
    def +(that: Matrix) = new Matrix(rows.zip(that.rows).map(arpair => arpair._1.zip(arpair._2).map(intpair => intpair._1 + intpair._2)))
    def *(that: Matrix) = new Matrix(rows.map(multiplyRow(_, that.rows)))

    // [1,2], [[3,4],[5,6]] returns [1*3 + 2*5, 1*4 + 2*6] = [13,16]
    private def multiplyRow(l: Array[Int], r: Array[Array[Int]]): Array[Int] =
      r.transpose.map(_.zip(l).map(intpair => intpair._1 * intpair._2).sum)

    def *(scalar: Int) = new Matrix(rows.map(_.map(_ * scalar)))
    def apply(row: Int, col: Int) = rows(row)(col)
    override def toString() = rows.map(_.mkString(", ")).mkString("\n")
  }
  object Matrix {
    def apply(values: Int*) = new Matrix(values.grouped(2).map(_.toArray).toArray)
  }

  val matr1 = Matrix(4, 1, 2, 7)
  val matr2 = Matrix(5, 4, 3, 2)
  println("Add matrices:")
  println(matr1)
  println("+")
  println(matr2)
  println("=")
  println(matr1 + matr2)
  println
  println("Transpose matrix:")
  println("matr1 transposed: \n" + matr1.transpose)
  println
  println("Multiply matrices:")
  println(matr1)
  println("*")
  println(matr2)
  println("=")
  println(matr1 * matr2)
  println
  println("Multiply scalar:")
  println(matr1)
  println("*")
  println("2")
  println("=")
  println(matr1 * 2)
  println
  println("Matrix class apply row and col:")
  println(matr1(0, 0) + ", " + matr1(0, 1) + ", " + matr1(1, 0) + ", " + matr1(1, 1))

  // 9. RichFile unapply filename parser extractor
  println("9. RichFile unapply")
  object RichFile {
    def unapply(input: File) = {
      val results = new Regex("(.+)/(.+)\\.(.+)", "path", "filename", "ext").findFirstMatchIn(input.getPath).get
      Some((results.group("path"), results.group("filename"), results.group("ext"))) // must return optional
    }
  }

  val RichFile(p, f, e) = new File("target/classes/chapter11.file.txt") // calls unapply
  println("path:      " + p)
  println("file:      " + f)
  println("extension: " + e)

  // 10. unapplySeq
  println("10. unapplySeq")
  object RichFileSeq {
    def unapplySeq(input: File): Option[Seq[String]] = Some(input.getPath.split("/"))
  }
  //  val RichFileSeq(pathsegment_*) = new File("target/classes/chapter11.file.txt") // _* matches a sequence
  val file = new File("target/classes/chapter11.file.txt")
  
  // match
  file match {
    case RichFileSeq(a, b, c) => println(a + ", " + b + ", " + c)
    case _                    => println("None")
  }
  
  // or
  val RichFileSeq(firstsegment, _*) = file // we dont care about the rest (_*)
  println(firstsegment)
  
  // or, searched it: http://daily-scala.blogspot.nl/2009/09/extract-sequences-unapplyseq.html
  val RichFileSeq(allseqments@_*) = file // the b@_* references all matched vars
  println(allseqments)

}