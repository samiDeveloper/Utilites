package bs.scalaimpatient

import math._

object Chapter02Scratch {

  // function, uses '=' in signature
  def fac(n: Int) = {
    var r = 1;
    for (i <- 1 to n) r = r * i;
    r;
  }

  def decorate(str: String, left: String = "[", right: String = "]") = { left + str + right }

  // * means vararg
  def sum(args: Int*) = {
    var r: Int = 0
    for (arg <- args) {
      r = r + arg;
    }
    r
  }

  // * means vararg
  // _* here means convert to vararg
  def recursiveSum(args: Int*): Int = {
    var r: Int = 0
    if (args.length == 0) 0
    else args.head + recursiveSum(args.tail: _*)
  }

  // no '=' means procedure, returns unit
  def box(s: String) {
    println("-" * s.length + "--")
    println("|" + s + "|")
    println("-" * s.length + "--")
  }

  def sumWithSideEffect(n: Int, m: Int) = {
    println("evaluate sumWithSideEffect")
    n + m
  }

  def sqrtThrowingExc(n: Int) = {
    if (n >= 0) sqrt(n)
    else throw new IllegalArgumentException("negative n not alowed")
  }

  def throwex() {
    println(sqrtThrowingExc(5))
    try {
      println(sqrtThrowingExc(-4))
    } catch {
      case ex: IllegalArgumentException => println(ex)  // use '_' for var name if not used
    }
  }
  
  def main(args: Array[String]) {

    throwex();
    
    //    val s1 = sumWithSideEffect(1, 2)
    //    println("eager val s1 was defined")
    //    println("accessing s1 value: " + s1)
    //
    //    lazy val s2 = sumWithSideEffect(1, 2)
    //    println("lazy val s2 was defined")
    //    println("accessing s2 value: " + s2)
    //
    //    box("AAARGGH")
    //    
    //    println(recursiveSum(4, 5, 6))
    //    
    //    println(sum(1 to 5: _*)) // Consider 1 to 5 as (convert to) an argument sequence
    //    println(sum(4, 3, 2, 1))
    //    
    //    println(decorate("ringnii", right = "!!!"))
    //    println(decorate(right = "}", str = "hnnng"))
    //    println(decorate(left = "{", str = "hnnng"))
    //    println(decorate("grr", "{"))
    //    println(decorate("grml", "<<<", ">>>"))
    //    println(decorate("aaargh"))
    //    
    //    println(fac(4))
  }

}
