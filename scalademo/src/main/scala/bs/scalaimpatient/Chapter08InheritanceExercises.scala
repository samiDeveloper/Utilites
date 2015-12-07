package bs.scalaimpatient

import scala.collection.mutable.ArrayBuffer
import java.awt.Rectangle

object Chapter08InheritanceExercises extends App {

  // 01. checkingaccount
  // aargh, cannot use floating point for money, so wrong
  class BankAccount(initialBalanceCents: Int) {
    private var balanceCt = initialBalanceCents
    def currentBalance = balanceCt
    def deposit(amountCents: Int) = { balanceCt += amountCents; balanceCt }
    def withdraw(amountCents: Int) = { balanceCt -= amountCents; balanceCt }
  }

  class CheckingAccount(initialBalanceCents: Int) extends BankAccount(initialBalanceCents) {
    override def deposit(amountCents: Int) = { super.deposit(amountCents - 100) }
    override def withdraw(amountCents: Int) = { super.withdraw(amountCents + 100) }
  }

  println("01. checkingaccount")
  val checkact = new CheckingAccount(100)
  println("bal: " + checkact.currentBalance)
  checkact.deposit(2000)
  println("bal: " + checkact.currentBalance)
  checkact.withdraw(50)
  println("bal: " + checkact.currentBalance)

  // 02. savingsaccount
  class SavingsAccount(initialBalanceCents: Int) extends BankAccount(initialBalanceCents) {
    private val monthlyinterest = 0.005
    private var monthTransactionsCount = 0
    def earnMonthlyInterest {
      monthTransactionsCount = 0
      super.deposit((currentBalance * monthlyinterest).toInt)
    }
    override def deposit(amountCents: Int) = { incMonthTransactionsCount; super.deposit(amountCents) }
    override def withdraw(amountCents: Int) = { incMonthTransactionsCount; super.withdraw(amountCents) }
    def incMonthTransactionsCount {
      if (monthTransactionsCount == 3) throw new RuntimeException("No way")
      monthTransactionsCount += 1
    }

  }

  println("02. savingsaccount")
  val savact = new SavingsAccount(10000)
  println("bal: " + savact.currentBalance)
  savact.earnMonthlyInterest
  println("bal: " + savact.currentBalance)
  savact.deposit(500)
  println("bal: " + savact.currentBalance)
  savact.withdraw(2000)
  println("bal: " + savact.currentBalance)
  savact.deposit(250)
  println("bal: " + savact.currentBalance)
  savact.earnMonthlyInterest
  println("bal: " + savact.currentBalance)
  savact.deposit(1)
  savact.deposit(1)
  savact.deposit(1)
  try {
    savact.deposit(1)
  } catch {
    case r: RuntimeException => println("Exception as expected: " + r.toString)
  }

  // 03. inheritance hierarchy 3 levels
  class Food {}
  class Meat extends Food {}
  class Plant extends Food {}
  class Mouse extends Meat {}
  class Buffalo extends Meat {}
  class Animal[F <: Food] {
    def eat(food: F) { println("Hap hap: " + food.toString) }
  }
  class Carnivore[M <: Meat] extends Animal[M] {}
  class Weasel extends Carnivore[Mouse] {} // cant say Plant here instead Mouse

  val w = new Weasel
  w.eat(new Mouse)
  // w.eat(new Plant)  does not compile as expected
  // w.eat(new Buffalo)  does not compile as expected

  // 04. item and bundle
  println("04. item and bundle")
  abstract class Item {
    def price(): Int
    def description(): String
  }

  class SimpleItem(val price: Int, val description: String) extends Item {
    override def toString(): String = "SimpleItem[price: " + price + ", description: " + description + "]"
  }

  class Bundle {
    val items: ArrayBuffer[Item] = new ArrayBuffer
    def add(item: Item) { items += item }
    def price() = {
      items.map(_.price).foldLeft(0)(_ + _)
    }
    override def toString() = {
      val sb = new StringBuilder("Bundle[price: " + price + ", containing " + items.size + " items: ")
      for (item <- items) sb append item.toString append ", "
      sb dropRight 2 append "]" toString
    }
    def description() = toString
  }

  val item1 = new SimpleItem(1, "item1")
  println(item1)
  val item2 = new SimpleItem(2, "item2")
  val bundle = new Bundle
  bundle.add(item1)
  bundle.add(item2)
  println(bundle.description)

  // 05. point
  println("05. point")
  class Point(val x: Int = 0, val y: Int = 0)
  class LabeledPoint(val label: String, x: Int = 0, y: Int = 0) extends Point(x, y)
  new LabeledPoint("label", 35, 12)

  // 06. shape
  println("06. shape")
  abstract class Shape {
    def centerPoint: (Double, Double)
  }
  class Rectangle(val upperLeft: (Double, Double), val downRight: (Double, Double)) extends Shape {
    def centerPoint: (Double, Double) = {
      ((downRight._1 - upperLeft._1) / 2 + upperLeft._1, (downRight._2 - upperLeft._2) / 2 + upperLeft._2)
    }
  }
  class circle(centre: (Double, Double), val radius: Double) extends Shape {
    def centerPoint: (Double, Double) = centre
  }

  val rect = new Rectangle((2, 2), (8, 4))
  println("rect centre: " + rect.centerPoint)

  // 07. square 
  class Square(leftUpper: (Int, Int), width: Int) extends java.awt.Rectangle(leftUpper._1, leftUpper._2, width, width) {
    def this(width: Int) = this((0, 0), width)
    def this() = this(0)
  }

  println("square: " + new Square)
  println("square: " + new Square(8))
  println("square: " + new Square((2, 3), 8))

  // 08. secret agent
  class Person8(val name: String) {
    override def toString = getClass.getName + "[name=" + name + "]"
  }
  class SecretAgent8(codename: String) extends Person8(codename) {
    override val name = "secret"
    override val toString = "secret"
  }

  // 09. creature
  println("09. creature")

  class Creature {
    val range: Int = 10
    val env: Array[Int] = new Array[Int](range)
  }
  class Ant extends Creature {
    override val range = 2
  }

  val ant = new Ant;
  println(ant.env.length)  // lenght 0 because the Cr constructor invokes the uninitialized Ant range
  // overriding Cr val with def results in ant.env.length = 0
  // overriding Cr and Ant val with def results in ant.env.length = 2
  
  // 10. Protected provides visibility from subclasses, not package like Java.
  // protected[this] provides access from subclasses only for this object
}

