package bs.scalaimpatient

import java.awt.Point

object Chapter06ObjectsExercises extends App {
  // 1. conversions
  object Conversions {
    def inchesToCentimeters(inches: Double) = 2.54 * inches
    def gallonsToLiters(gallons: Double) = 3.78541178 * gallons
    def milesToKilometers(miles: Double) = 1.609344 * miles
  }

  // 2. conversions classes
  abstract class UnitConversion {
    def convert(value: Double): Double;
  }

  class InchesToCentimeters extends UnitConversion {
    override def convert(value: Double) = 2.54 * value
  }

  class GallonsToLitres extends UnitConversion {
    override def convert(value: Double) = 3.78541178 * value
  }

  class MilesToKilometers extends UnitConversion {
    override def convert(value: Double) = 1.609344 * value
  }

  println("20 miles = " + new MilesToKilometers().convert(20.0) + " kms")

  // 3. origin
  object Origin extends java.awt.Point

  // 4. origin companion
  class Point private (val x: Int, val y: Int)

  object Point {
    def apply(x: Int, y: Int) = new Point(x, y)
  }

  val p = Point(3, 4)

  // 5. print args App
  object Reverse extends App {
    for (i <- (args.length - 1) to (0, -1)) print(args(i) + " ")
  }

  Reverse.main(Array("A", "B", "C"))
  println

  // 6. 
  object CardSuit extends Enumeration {
    val SPADE = Value("\u2660")
    val HEART = Value("\u2665")
    val DIAMOND = Value("\u2666")
    val CLUB = Value("\u2663")
  }

  println("6. card suit: " + CardSuit.values)

  // 7. red?
  def red(card: CardSuit.Value) = card == CardSuit.HEART || card == CardSuit.DIAMOND
  println("Spade is red: " + red(CardSuit.SPADE))
  println("Diamond is red: " + red(CardSuit.DIAMOND))
  
  // 8. 
  object ColorCubeCorner extends Enumeration {
    val WHT = Value(0xffffff)
    val RG = Value(0xffff00)
    val RB = Value(0xff00ff)
    val RED = Value(0xff0000)
    val GB = Value(0x000000)
    val G = Value(0x00ff00)
    val B = Value(0x0000ff)
    val BLK = Value(0x00ffff)
  }

}