package bs.scalaimpatient

object Chapter12HigherOrderFunctionsExercises extends App {
  // 1. values function
  println("1. values function")
  def values(fun: (Int) => Int, low: Int, high: Int) = {
    low.to(high).map(s => (s, fun(s))) // we need to name s here cannot use _
  }
  println(values(x => x * x, -5, 5))

  // 2. reduceLeft max
  println("2. reduceLeft max")
  println("max: " + Array(3, 5, 1, 2).reduceLeft(_.max(_)))

  // 3. factorial function reduceLeft (faculteit)
  println("3. factorial function reduceLeft")
  def factorialReduce(n: Int) = {
    if (n < 0) -1
    else if (n == 0) 1 // how does scala support empty product? (= 1) http://stackoverflow.com/questions/25088291/why-doesnt-unit-extend-product-in-scala
    else 1.to(n).reduceLeft(_ * _)
  }

  println("fact reduce -4: " + factorialReduce(-4))
  println("fact reduce 0: " + factorialReduce(0))
  println("fact reduce 1: " + factorialReduce(1))
  println("fact reduce 4: " + factorialReduce(4))

  // 4. factorial foldLeft
  println("4. factorial foldLeft")
  def factorialFold(n: Int) = {
    if (n < 0) -1
    else (1 to n).foldLeft(1)(_ * _) // returns one on empty product
  }

  println("fact fold -4: " + factorialFold(-4))
  println("fact fold 0: " + factorialFold(0))
  println("fact fold 1: " + factorialFold(1))
  println("fact fold 4: " + factorialFold(4))

  // 5. largest
  println("5. largest")
  def largest(fun: (Int) => Int, inputs: Seq[Int]) = inputs.map(fun(_)).max
  println("largest of 6 / n, (1,2,3): " + largest(n => 6 / n, 1 to 3))
  println("largest of 1 * n, (1,2,3): " + largest(n => 1 * n, 1 to 3))
  println("largest of 10 * n - n * n, (1...10): " + largest(n => 10 * n - n * n, 1 to 10))

  // 6. largest yield input, yields the input for which the function produces the largest value
  println("6. largest yield input")

  // maps the pair to an object for which exists an implicit ordering: the second Int of the pair
  def largestYieldInput(fun: (Int) => Int, inputs: Seq[Int]) = inputs.map(n => (n, fun(n))).max(Ordering.by[(Int, Int), Int](_._2))._1
  println("input for largest of 6 / n, (1,2,3): " + largestYieldInput(n => 6 / n, 1 to 3))
  println("input for largest of 1 * n, (1,2,3): " + largestYieldInput(n => 1 * n, 1 to 3))
  println("input for largest of 10 * n - n * n, (1...10): " + largestYieldInput(n => 10 * n - n * n, 1 to 10))

  // 7. adjust to pair
  println("7. adjust to pair")
  def adjustToPair(fun: (Int, Int) => Int): ((Int, Int)) => Int = (t2: (Int, Int)) => fun(t2._1, t2._2)
  println("adjustToPair(_ * _)((6, 7)): " + adjustToPair(_ * _)((6, 7))) // 42
  val pairs = (1 to 10) zip (11 to 20)
  println("mapped pairs: " + pairs.map(adjustToPair(_ * _)(_))) // Vector(11, 24, 39, 56, 75, 96, 119, 144, 171, 200)

  // 8. corresponds call
  println("8. corresponds call")
  val strings = Array("Lorem", "ipsum", "dolor", "sit", "amet")
  val lens1 = Array(5, 5, 5, 3, 4)
  val lens2 = Array(5, 5, 4, 3, 4)
  println("lens1 corresponds: " + strings.corresponds(lens1)((s, l) => s.length() == l))
  println("lens2 corresponds: " + strings.corresponds(lens2)((s, l) => s.length() == l))

  // 9. corresponds no currying
  println("9. corresponds no currying")
  def correspondsAllAtOnce[A, B](fun: (A, B) => Boolean, l: Seq[A], r: Seq[B]): Boolean = (l zip r).foldLeft(true)((l, r) => l & fun(r._1, r._2))

  // Now we have to specify types String and Int in the function definition passed to corresponds
  println("lens1 correspondsAllAtOnce: " + correspondsAllAtOnce((s: String, l: Int) => s.length() == l, strings, lens1))
  println("lens2 correspondsAllAtOnce: " + correspondsAllAtOnce((s: String, l: Int) => s.length() == l, strings, lens2))

  // 10. unless control abstraction
  println("10. unless control abstraction")
  def unless(condition: => Boolean)(block: => Unit) {
    if (!condition) {
      block
    }
  }
  
  val x = 1
  unless(x == 2) {
    println("x != 2")
  }

  // what if condition is call-by-value and block is not curried
  def unless_crippled(condition: () => Boolean, block: => Unit) {
    if (!condition()) {
      block
    }
  }
  
  val y = 1
  unless_crippled(() => y == 2, {
    println("y != 2")
  })
}