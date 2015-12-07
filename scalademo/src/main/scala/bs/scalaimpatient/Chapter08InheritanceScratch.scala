package bs.scalaimpatient

object Chapter08InheritanceScratch extends App {
  class Person {

    // great, omitting override yields: "overriding method toString in class Object of type ()String; method toString needs `override' modifier"
    override def toString = { super.toString + ": person" }
  }
  println(new Person)

  class Employee extends Person {}
  val emp = new Employee
  println("Emp instanceOf Person: " + emp.isInstanceOf[Person]) // true
  println("Emp is Person type: " + (emp.getClass == classOf[Person])) // false

  // pattern matching
  emp match {
    case s: Employee => println("Employee")
    case _           => println("Not an Employee")
  }

  // override field...
  abstract class Person2 {
    def id: Int // abstract method, just omit the body
  }

  // subclasses need not use the override keyword for overriding an abstract member, but it is allowed
  class Student(override val id: Int) extends Person2

  // an anonymous subclass is of structural type: 'Student{def greeting: String}' this is actually a type
  val student = new Student(1) {
    def greeting = "Hi from anonymous sub student"
  }

  def acceptStudentOfferingGreeting(s: Student { def greeting: String }) {
    println(s.greeting)
  }

  acceptStudentOfferingGreeting(student)

  // abstract field
  abstract class Person3 {
    val id: Int // omitting assignment makes field abstract, abstract getter. generated class has no fields
  }

  // Unit and Any
  def printAny(x: Any) { println(x) }
  def printUnit(x: Unit) { println(x) } // prints '()'
  printAny("Hello Any")
  printUnit("Hello Unit")

  // eclipse generated equals and hashcode
  class Person4(val id: Int) extends Equals {
    def canEqual(other: Any) = {
      other.isInstanceOf[bs.scalaimpatient.Chapter08InheritanceScratch.Person4]
    }

    override def equals(other: Any) = { // make sure use Any
      other match {
        case that: bs.scalaimpatient.Chapter08InheritanceScratch.Person4 => that.canEqual(Person4.this) && id == that.id
        case _ => false
      }
    }

    override def hashCode() = {
      val prime = 41
      prime + id.hashCode
    }

  }

  val p4a = new Person4(1)
  val p4b = new Person4(1)

  // prefered way is to just use '=='
  println("p4a == p4b: " + (p4a == p4b)) // '==' calls equals
  println("p4a == p4b: " + p4a == p4b) // ! note how this yields the wrong result if we forget the parentheses
  println("null == p4b: " + (null == p4b)) // this is allowed
}