package bs.scalaimpatient

import scala.beans.BeanProperty
import sun.org.mozilla.javascript.internal.BeanProperty

object Chapter05ClassesExercises extends App {
  // 1. counter
  class Counter {
    private var value: Int = 0

    def inc() {
      value = if (value == Int.MaxValue) 0 else value + 1
    }

    def current() = value
  }

  // 2. bank account
  class BankAccount(private[this] var balance: Int = 0) {
    def deposit(cents: Int) { balance += cents }
    def withdraw(cents: Int) { balance -= cents }
  }

  // 3. the future is our time
  class TimeExerciseThree(private val hours: Int, private val minutes: Int) {
    if (hours > 23 || minutes > 59) throw new IllegalArgumentException();
    def before(other: TimeExerciseThree) = {
      hours * 60 + minutes < other.hours * 60 + other.minutes
    }
  }

  // 4. this is the time of inevitability
  class Time(private val hours: Int, private val minutes: Int) {
    if (hours > 23 || minutes > 59) throw new IllegalArgumentException();
    private val mins = hours * 60 + minutes;
    def before(other: Time) = {
      mins < other.mins
    }
  }

  // test time
  val begin = new Time(12, 15)
  val end = new Time(15, 5)
  println("begin before end: " + (begin before end))
  println("end before begin: " + (end before begin))
  println

  try {
    new Time(24, 0)
  } catch {
    case ex: IllegalArgumentException => print("OK, expected exception")
  }

  // 5. student
  println("5. student")
  class Student(@BeanProperty var name: String, @BeanProperty var id: Int) {
  }

  val stud = new Student("Arie", 23)
  stud.setName("Updated")

  // bs@bs:~/repos/public-sandbox/scalademo/target/classes/bs/scalaimpatient$ javap -private Chapter05ClassesExercises\$Student.class

  // 6. person age
  println("6. person")
  class PersonAge(private var age: Int) {
    if (age < 0) age = 0
    override def toString = "Person, age = " + age
  }

  println(new PersonAge(18))
  println(new PersonAge(-12))

  // 7. person name
  println("7. person name")
  class Person(private val fullName: String) {
    private val splitName: Seq[String] = fullName.split(" ")
    private val firstName: String = splitName(0)
    private val lastName: String = splitName(1)
    override def toString = "Person, firstName: " + firstName + ", lastName: " + lastName
  }

  println(new Person("John Smith"))

  // 8. car, 4 constructors, why?
  class Car(val manufacturer: String, val modelName: String, val modelYear: Int = -1, var licencePlate: String = "") {
    override def toString = "Car: manuf: " + manufacturer + ", model: " + modelName + ", year: " + modelYear + ", licence: " + licencePlate
  }
  val car = new Car("Volvo", "V60")
  car.licencePlate_=("plate")
  println(car)

  // Ok then
  class CarConstructors(val manufacturer: String, val modelName: String, val modelYear: Int, var licencePlate: String) {
    def this(manufacturer: String, modelName: String, modelYear: Int) {
      this(manufacturer, modelName, modelYear, "")
    }
    def this(manufacturer: String, modelName: String, licencePlate: String) {
      this(manufacturer, modelName, -1, licencePlate)
    }
    def this(manufacturer: String, modelName: String) {
      this(manufacturer, modelName, -1, "")
    }
    override def toString = "CarConstructors: manuf: " + manufacturer + ", model: " + modelName + ", year: " + modelYear + ", licence: " + licencePlate
  }
  val carc = new CarConstructors("Volvo", "V60")
  carc.licencePlate_=("plate")
  println(carc)

  // 9. much shorter

  // 10. employee default primary constructor
  class Employee {
    private var m_name: String = ""
    private var m_salary: Double = 0.0

    def this(name: String, salary: Double) {
      this()
      this.m_name = name
      this.m_salary = salary
    }

    def name() = m_name
    def salary() = m_salary
  }

  println("10. employee")
  val emp = new Employee("John", 2576.25)
  println(emp.name + " " + emp.salary)
}