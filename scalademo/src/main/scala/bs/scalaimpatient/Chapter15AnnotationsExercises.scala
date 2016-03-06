package bs.scalaimpatient

import org.junit.Test
import junit.framework.Assert
import scala.io.Source
import java.io.IOException
import java.util.concurrent.CountDownLatch
import scala.annotation.tailrec

object Chapter15AnnotationsExercises extends App {
  // 1. junit 
  println("1. junit")
  @Test def testDummy1() { Assert.fail("NYI") }
  @Test(timeout = 100) def testDummy2() { Assert.fail("NYI") }
  @Test(expected = classOf[IllegalArgumentException]) def testDummy3() { Assert.fail("NYI") }

  // 2. annotatable items
  println("2. annotatable items")
  @deprecated class annotatedClass @deprecated() (@deprecated val value: String) { // the primary constructor annotation needs parenths if it has no args
    @deprecated val desc: String = ""
    @deprecated def process(@deprecated in: String) {}
  }

  // 3. scala annotations using meta-annotations
  // Inspect the annotation.Annotation subclasses:
  // BooleanBeanProperty, deprecatedName, deprecated, compileTimeOnly, transient, volatile

  // 4. sum varargs
  println("4. sum varargs")
  // ??? @varargs has no effect, it generates a sum(Seq<Object) instead of sum(Integer...), see Chapter15Annotations.java
  @annotation.varargs def sum(i: Int*): Int = {
    i.sum
  }
  println(sum(1, 2, 3))

  // 5. read file
  println("5. read file")
  // whats this got to do with annotations? its probably about the throws
  @throws(classOf[IOException]) def readResource(filename: String): String = {
    Source.fromFile(filename, "UTF-8").getLines.mkString
  }

  // 6. volatile
  println("6. volatile")
  // ??? nothing special happens if fld is not volatile
  object Subject {
    @volatile var fld: Boolean = false
  }
  val done = new CountDownLatch(1)
  new Thread {
    override def run() {
      while (!Subject.fld) {
        println("still false...")
        Thread.sleep(100)
      }
      println("true now, quitting")
      done.countDown
    }
  }.start
  new Thread {
    override def run() {
      Thread.sleep(500)
      Subject.fld = true
      println("set true")
    }
  }.start
  done.await

  // 7. tailrec
  println("7. tailrec")
  class Util {
    @tailrec final def sum(x: Seq[Int], partial: BigInt): BigInt = { // must declare as final
      if (x.isEmpty) partial else sum(x.tail, x.head + partial)
    }
  }
  val util = new Util
  println(util.sum(1 to 10000, 0))

  // 8. @specialized
  println("8. @specialized")
  object AllDifferent {
    def allDifferent[@specialized T](x: T, y: T, z: T) = x != y && x != z && y != z
  }
  // javap -private Chapter15AnnotationsExercises\$AllDifferent\$.class:
  // public <T> boolean allDifferent(T, T, T);
  // @specialized adds:
  // public boolean allDifferent$mZc$sp(boolean, boolean, boolean);
  // public boolean allDifferent$mBc$sp(byte, byte, byte);
  // public boolean allDifferent$mCc$sp(char, char, char);
  // ...

  // 9. 
  // 10. factorial assert
  println("10. factorial assert")
  def factorial(n: Int): Int = { assert(n >= 0); if (n == 0) n else n * factorial(n - 1) }
  //  factorial(-1)
}