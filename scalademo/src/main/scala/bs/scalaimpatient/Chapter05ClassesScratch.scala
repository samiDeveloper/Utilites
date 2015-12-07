package bs.scalaimpatient

import scala.beans.BeanProperty

class Counter {
  private var value = 0
  def inc() = { value += 1; this }
  def current() = value // client may call without parantheses
  def cur = value // client nust call without parenths

  var age = 0 // generates getter def age() and setter age_=(a:Int)
}

object CounterTester extends App {
  val c = new Counter
  c.inc().inc()
  println("counter: " + c.current)

  c.age = 21 // calls c.age_=(a:Int)
  c.age_=(c.age + 1)
  c age_= c.age + 1
  println("c age: " + c.age)
}

class Person {
  // generate javabean getter and setter
  @BeanProperty var name: String = _
}

object PersonTester extends App {
  val p = new Person
  p.name_=("Henk")
  p.setName("Arie")
  p name_= "Jofel"
  p.name
  p.getName
  p name
}

class Address {
  private var street = ""
  private var city = ""

  // auxiliary constructor #1, order is relevant
  def this(street: String) {
    this()
    this.street = street;
  }

  // aux constructor #2, must call #1
  def this(street: String, city: String) {
    this(street)
    this.city = city
  }
}

class Customer(private[this] val name: String, private val shoeSize: Byte = 42, var score: Double = 0.0) {
  println("Constructing Customer")
  private val shortDesc = "Customer " + name + " having shoe size " + shoeSize
  def describeStatic(): String = { shortDesc }

}

object CustomerTest extends App {
  val c = new Customer("Dirk", 42, 6.7)
  c.score_=(4.3)
  println(c.describeStatic)

  val another = new Customer("Arie", 41)
  println(another.describeStatic)
}

// no val or var means an immutable private[this] (object private) field, only if used in at least one method, so:
// name will be a object private field, and number will not
class Thing(name: String, number: Int = 0) {
  def describeShort = name

  def compare(other: Thing): Int = {
    // cannot other.name
    0
  }
}

object ThingTester extends App {
  val t = new Thing("grmbl", 3)
}