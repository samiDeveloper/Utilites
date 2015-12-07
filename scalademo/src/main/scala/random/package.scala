// For Chapter07PackagesExercises ex 3.
package object random {

  var previous: Double = 0

  val a: Int = 1664525
  val b: Int = 1013904223
  val n: Int = 32

  def nextInt(): Int = {
    next.toInt
  }

  def nextDouble(): Double = {
    next
  }
  
  private def next(): Double = {
    previous = (previous * a * b) % math.pow(2, n)
    previous
  }

  def setSeed(seed: Int) {
    this.previous = seed
  }
}