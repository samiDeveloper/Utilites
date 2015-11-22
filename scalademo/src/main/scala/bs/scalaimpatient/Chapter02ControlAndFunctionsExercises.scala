package bs.scalaimpatient

object Chapter02Exercises extends App {
  // 1
  //    println(signum(-2))
  //    println(signum(0))
  //    println(signum(5))
  def signum(n: Int) = {
    if (n < 0) -1
    else if (n > 0) 1
    else 0
  }

  // 2
  val v = {}
  println(v) // type is Unit, value is '()' = "no useful value"

  // 3
  var y = 1
  val x = y = 1
  println(x)

  // 4
  for (i <- 10.to(0, -1)) print(i + " ")
  println
  for (i <- 10 to (0, -1)) print(i + " ")
  println

  // 5
  countdown(5)
  println

  def countdown(arg: Int) {
    for (n <- arg to (0, -1)) print(n + " ")
  }

  // 6
  val s6 = "Hello"
  var prod6 = 1
  for (c <- s6) prod6 *= c
  println(prod6)

  // 7
  var prod7 = 1;
  "Hello".foreach(c => prod7 = prod7 * c)
  println(prod7)

  // 8
  def product(s: String) = {
    var prod = 1
    s.foreach(c => prod = prod * c)
    prod
  }
  println(product("Hello"))

  // 9
  def productRec(s: String): Int = {
    if (s.length() == 0) 1
    else s.head * productRec(s.tail)
  }
  println("recursive: " + productRec("Hello"))

  // 10
  def pow(x: Double, n: Int): Double = {
    if (n > 0)
      if (n % 2 == 0) pow(x, n / 2) * pow(x, n / 2)
      else x * pow(x, n - 1)
    else if (n == 0) 1
    else /*(n < 0)*/ 1 / pow(x, -n)
  }
  println("pow: " + pow(2, 3))
  println("pow: " + pow(2, 0))
  println("pow: " + pow(2, -2))
}