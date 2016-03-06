package bs.scalaimpatient

import scala.collection.mutable.ArrayBuffer

object Chapter18AdvancedtypesExercises extends App {
  {
    // 1. bug
    println("1. bug")
    class Bug {
      private var pos = 0
      private var direction = +1
      def move(steps: Int) = { pos += (direction * steps); this }
      def show() = { print(pos + " "); this }
      def turn() = { direction *= -1; this }

    }

    val bugsy = new Bug
    bugsy.move(4).show().move(6).show().turn().move(5).show()
    println
  }

  {
    // 2. [aborted] fluent bug 
    println("2. [aborted] fluent bug")
    class Bug {
      private var pos = 0
      private var direction = +1
      private var command: Any = null
      def move(steps: Int) = { pos += (direction * steps); this }
      def and = { this }
      def show() = { print(pos + " "); this }
      def turn() = { direction *= -1; this }

      private def domove(steps: Int) = { pos += (direction * steps); this }
    }

    val bugsy = new Bug

    // bugsy move 4 and show  // error 'Bug does not take parameters', how to incorporate a fluent no-args call?
  }

  {
    // 3. fluent document 
    object Title
    object Author
    class Document {
      private var title: String = ""
      private var author: String = ""
      private var command: Any = null
      def set(cmd: Any): this.type = { this.command = cmd; this }
      def to(value: String): this.type = {
        if (command == Title) {
          this.title = value;
        } else if (command == Author) {
          this.author = value
        }
        this
      }
    }

    class Book extends Document {
      private var chapter: String = ""
      def addChapter(chapter: String) = {
        this.chapter = chapter
        this
      }
    }

    val book = new Book
    book set Title to "Scala for the Impatient" set Author to "Cay Horstmann"
  }

  {
    // 4. type projections equals
    println("4. type projections equals")

    class Network {
      outer =>
      class Member(val name: String) {
        val contacts = new ArrayBuffer[Member]
        private val network = outer
        override def equals(other: Any) = {
          if (!other.isInstanceOf[Member] || this.network != other.asInstanceOf[Member].network) { false }
          else {
            val that = other.asInstanceOf[Member]
            val namesequal = name.equals(that.name)
            namesequal; // ignores contacts 
          }
        }
      }
      val members = new ArrayBuffer[Member]
      def join(name: String) = {
        val m = new Member(name)
        members += m
        m
      }
    }

    val chatter = new Network
    chatter.join("a")

    val myFace = new Network
    myFace.join("a")

    println(chatter.members(0).equals(myFace.members(0))) // same name, false because not from same network instance
  }

  {
    // 6. either as infix type
    println("6. either as infix type")

    // returns either the element's index or index of closest value
    def find(source: Array[Int], element: Int): Int Either Int = {
      var diff = -1
      var index = 0;
      for (i <- 1 to 3) {
        val curdiff = source(index).max(element) - source(index).min(element)
        if (diff == -1 || curdiff < diff) {
          index = i
          diff = curdiff
        }
      }
      if (diff == 0) { Left(index) } else { Right(index) }
    }

    println(find(Array(1, 3, 6, 8), 4))
    println(find(Array(1, 3, 6, 8), 6))
  }

  {
    // 7. structural type close
    println("7. structural type close")
    def processAndClose[T <: { def close() }, R](target: T, fun: (T) => R): R = {
      val result = fun.apply(target)
      target.close()
      result
    }
  }

  {
    // 8. structural type apply
    println("8. structural type apply")
    def printValues[T <: { def apply(i: Int): Int }](fun: T, from: Int, to: Int) {
      for (i <- from to to) { print(fun.apply(i) + " ") }
    }
    printValues((x: Int) => x * x, 3, 6)
  }

  {
    // 9. knucklehead
    println("9. knucklehead")
    abstract class Dim[T](val value: Double, val name: String) {
      this: T =>
      protected def create(v: Double): T
      def +(other: Dim[T]) = create(value + other.value)
      override def toString() = value + " " + name;
    }

    class Seconds(v: Double) extends Dim[Seconds](v, "s") {
      override def create(v: Double) = { new Seconds(v) }
    }

    // 'this: T =>' prevents this from compiling:
    //    class Meters(v: Double) extends Dim[Seconds](v, "m") {
    //      override def create(v: Double) = new Seconds(v)
    //    }
  }
  
  {
    
  }

}