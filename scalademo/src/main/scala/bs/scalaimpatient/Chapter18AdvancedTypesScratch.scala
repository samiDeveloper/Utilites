package bs.scalaimpatient

import scala.collection.mutable.ArrayBuffer
import javax.swing.JComponent
import scala.io.Source

object Chapter18AdvancedTypesScratch extends App {
  {
    // singleton types: v.type
    class Document {
      def setTitle(title: String) = {
        // ...
        this // anable fluent
      }

      // this.type upgrades the return type from this.type to subclass.type (~ Java getThis trick)
      def setAuthor(author: String): this.type = {
        // ...
        this
      }
    }

    class Book extends Document {
      def addChapter(chapter: String) = {
        // ...
        this
      }
    }

    val book = new Book()

    //  book.setTitle("title").addChapter("chapter") // won't compile because return type of setTitle is this
    book.setAuthor("author").addChapter("chapter") // compiles because return type of setAuthor is this.type
  }

  class Network {
    class Member(val name: String) {
      val contacts = new ArrayBuffer[Member]
    }
    private val members = new ArrayBuffer[Member]
    def join(name: String) = {
      val m = new Member(name)
      members += m
      m
    }
  }

  val chatter = new Network
  val myFace = new Network

  {
    // type projections
    val joe = chatter.join("Joe")
    val guido = myFace.join("Guido Brasletti")

    //            joe.contacts += guido // won't compile
    // use val contacts = new ArrayBuffer[Network#Member] to allow members of other networks in this collection

  }

  {
    // paths, in path below, all parts before the final type Member are stable: package.object.val
    // would not compile if Chapter18AdvancedTypesScratch was a class or if chatter was a var
    val t = new bs.scalaimpatient.Chapter18AdvancedTypesScratch.chatter.Member("name")

  }

  {
    // type aliases
    type ChattersMember = bs.scalaimpatient.Chapter18AdvancedTypesScratch.chatter.Member
    val t = new ChattersMember("name")
  }

  {
    // structural types: runtime checks using reflection, use only if adding trait is impossible
    def appendLines(target: { def append(str: String): Any },
                    lines: Iterable[String]) {
      for (l <- lines) { target.append(l) }
    }
  }

  {
    // compount types
    val image = new ArrayBuffer[java.awt.Shape with java.io.Serializable]
  }

  {
    // infix type: mathemetical notation
    // infix syntax:
    type stringintmap1 = String Map Int
    // means:
    type stringintmap2 = Map[String, Int]
  }

  {
    // existential types, compatibility with Java wildcards
    type a1 = Array[T] forSome { type T <: JComponent }
    // same as scala wildcard notation (ch.17)
    type a2 = Array[_ <: JComponent]
    // but forSome allows for more complex relationships
  }

  {
    // scala type system, turn method into function using _
    def square(x: Int): Int = x * x
    println("square method: " + square(2))

    val squarefun = square _ // it has type (Int) => Int
    def exec(fun: (Int) => Int, x: Int) = { fun.apply(x) }
    println("square fun: " + exec(squarefun, 2))
  }

  {
    // self  types
    trait Logged {
      def log(msg: String)
    }

    // only mix into a subclass of Exception
    trait LoggedException extends Logged {
      this: Exception =>
      def log() { log(getMessage()) }
    }

    {
      // abstract types
      trait Reader {
        type Contents   // make concrete in subclass
        def read(fileName: String): Contents
      }

      class StringReader extends Reader {
        type Contents = String
        def read(fileName: String) = Source.fromFile(fileName, "UTF-8").mkString
      }
      
      // same as using type parameter, which to choose?
      // use type params if the type is supplied when the class is *instantiated*
      // use abstract types when types are specified by a *subclass*, avoids a long list of parameters
      
      
      
    }
  }

}