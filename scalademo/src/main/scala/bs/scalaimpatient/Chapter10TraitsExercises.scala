package bs.scalaimpatient

import java.awt.geom.Ellipse2D
import java.awt.Point
import scala.collection.immutable.BitSet
import scala.io.Source
import java.beans.PropertyChangeSupport
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import java.io.InputStream
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

object Chapter10TraitsExercises extends App {
  // 01. RectangleLike
  println("01. RectangleLike")
  trait RectangleLike {
    def getX(): Double
    def getY(): Double
    def getWidth(): Double
    def getHeight(): Double
    def setFrame(x: Double, y: Double, w: Double, h: Double)
    def translate(dx: Double, dy: Double) {
      setFrame(getX + dx, getY + dy, getWidth, getHeight)
    }
    def grow(horiz: Double, vert: Double) {
      setFrame(getX - horiz, getY - vert, getWidth + 2 * horiz, getHeight + 2 * vert)
    }
    override def toString = "[%f, %f, %f, %f]".format(getX, getY, getWidth, getHeight)
  }

  val egg = new Ellipse2D.Double(5, 10, 20, 30) with RectangleLike
  println(egg)
  egg.translate(10, 15)
  println(egg)
  egg.grow(3, 7)
  println(egg)

  // 02. OrderedPoint
  println("02. OrderedPoint")
  class OrderedPoint extends Point with Ordered[Point] {
    def compare(that: Point): Int = {
      if (getX == that.getX && getY == that.getY) 0
      else if (getX <= that.getX && getY < that.getY) -1
      else 1
    }
  }

  // 03. BitSet linearization
  println("03. BitSet linearization")
  println("linearization of BitSet: BitSet >> lin(BitSetLike) >> lin(collection.BitSet) >> lin(SortedSet) >> lin(AbstractSet)")
  println("Diagram:")
  println(Source.fromInputStream(getClass.getResourceAsStream("/chapter10.bitset.txt")).mkString)

  // 04. CryptoLogger Ceasar cipher
  println("04. CryptoLogger Ceasar cipher")
  trait Logger { def log(msg: String) }
  trait ConsoleLogger extends Logger {
    override def log(msg: String) { println(msg) }
  }
  trait CryptoLogger extends Logger {
    abstract override def log(msg: String) { // abstract override for super.log, it forces a concrete logger to be mixed in
      super.log(encryptString(msg, 3))
    }
    def encryptString(text: String, key: Short): String = {
      text.map(encrypt(_, key))
    }
    def encrypt(ch: Char, key: Short): Char = {
      val chars = ' ' to '~'
      chars((chars.indexOf(ch) + key) % chars.size)
    }
  }
  class Logged extends ConsoleLogger {
    def doit(msg: String) {
      log(msg)
    }
  }
  val logged = new Logged
  logged.doit("AAARGH")

  val cryptologged = new Logged with CryptoLogger
  cryptologged.doit("AAARGH")

  // 05. PropertyChangeSupport trait
  println("05. PropertyChangeSupport trait")
  trait PropertyChangeSupportTrait {
    val pcs = new PropertyChangeSupport(this)
    def addListener(lst: PropertyChangeListener) {
      pcs.addPropertyChangeListener(lst)
    }
    def removeListener(lst: PropertyChangeListener) {
      pcs.removePropertyChangeListener(lst)
    }
  }

  class PointWithPropertyChangeSupport extends Point with PropertyChangeSupportTrait {
    override def move(x: Int, y: Int) {
      val oldX = this.x
      val oldY = this.y
      super.move(x, y)
      fireNewPosition(oldX, oldY)
    }

    // only support move for now, override other state changers here...

    def fireNewPosition(oldX: Int, oldY: Int) {
      pcs.firePropertyChange("x", oldX, x)
      pcs.firePropertyChange("y", oldY, y)
    }
  }

  val point = new PointWithPropertyChangeSupport
  val lst = new PropertyChangeListener {
    override def propertyChange(evt: PropertyChangeEvent) {
      println("event: " + evt)
    }
  }
  point.addListener(lst)
  point.move(10, 20)

  // 06. JComponent
  // A JPanel for instance would need multiple inheritance to extend JComponent and JContainer
  // Implement JContainer as a trait. Have JComponent extend Component, and JPanel extend Component with JContainer

  // 07. Silly trait hierarchy

  // 08. Buffered trait
  println("08. Buffered trait")
  trait BufferedInputStreamTrait {
    This: InputStream =>
    val bis = new BufferedInputStream(this)
    override def read(): Int = { bis.read() }
    override def mark(readlimit: Int) { bis.mark(readlimit) }
    override def reset() { bis.reset() }
  }

  {
    val bufferedis = new FileInputStream("target/classes/chapter10.bufferedtrait.txt") with BufferedInputStreamTrait
    val baos = new ByteArrayOutputStream

    for (i <- 1 to 3) { baos.write(bufferedis.read) }
    bufferedis.mark(100)
    for (i <- 1 to 16) { baos.write(bufferedis.read) }
    bufferedis.reset // repeats 'bewolking wordt '
    for (i <- 1 to 40) { baos.write(bufferedis.read) }
    val out = new String(baos.toByteArray)
    println(out)
  }

  // 09. add logging to InputStream buffering
  println("09. add logging to InputStream buffering");
  {
    val bufferedis = new FileInputStream("target/classes/chapter10.bufferedtrait.txt") with BufferedInputStreamTrait with ConsoleLogger {
      override def mark(readlimit: Int) { log("Mark inputstream to buffer max " + readlimit + " bytes"); super.mark(readlimit) }
      override def reset() = { log("Reset inputstream to last mark"); super.reset() }
    }
    val baos = new ByteArrayOutputStream

    for (i <- 1 to 3) { baos.write(bufferedis.read) }
    bufferedis.mark(100)
    for (i <- 1 to 16) { baos.write(bufferedis.read) }
    bufferedis.reset // repeats 'bewolking wordt '
    for (i <- 1 to 40) { baos.write(bufferedis.read) }
    val out = new String(baos.toByteArray)
    println(out)
  }

  // 10. IterableInputStream
  println("10. IterableInputStream")
  abstract class IterableInputStream extends InputStream with Iterable[Byte] {
    class InputStreamIterator(is: InputStream) extends Iterator[Byte] {
      override def hasNext: Boolean = is.available > 0
      override def next: Byte = is.read.toByte
    }
    def iterator: Iterator[Byte] = new InputStreamIterator(this)
  }
}

















