package bs.scalaimpatient

object Chapter14PatternMatchingScratch extends App {
  {
    val ch = '-'
    val sign = ch match {
      case '+' => 1
      case '-' => -1
      case _   => 0
    }
    println("sign: " + sign)
  }

  {
    val ch = '4'
    var sign = 1
    var digit = 0
    ch match {
      case '-'                          => sign = -1
      case '+'                          => sign = 1
      case _ if (Character.isDigit(ch)) => digit = Character.digit(ch, 10)
      case _                            => sign = 0
    }
    println("digit: " + digit)
  }

  {
    val str = "4+3"
    val i = 2
    var sign = 1
    var digit = 0
    str(i) match {
      case '+'                         => sign = 1
      case '-'                         => sign = -1
      case ch if Character.isDigit(ch) => digit = Character.digit(ch, 10)
      case _                           => sign = 0
    }
    println("var in pattern: " + digit)
  }

  // type pattern
  {
    val sw: Any = "24"
    val num = sw match {
      case i: Int    => i
      case s: String => Integer.parseInt(s)
      case _         => 0
    }
    println("num: " + num.getClass() + " = " + num)
  }

  // match array
  {
    val ar = Array(0, 7, 8)
    val res = ar match {
      case Array(0)     => "zero array"
      case Array(x, y)  => x + ", " + y
      case Array(0, _*) => "0, ..."
      case _            => "?"
    }
    println("match array: " + res)

  }

  // match list
  {
    val in = List(0, 4)
    val out = in match {
      case List(0)    => "0"
      case List(x, y) => x + ", " + y
      case 0 :: tail  => "0 ..."
      case _          => "?"
    }
    println("match list: " + out)
  }

  // array extractor
  {
    val x = Array.unapplySeq(Array(3, 4, 5))
    println("array extr: " + x)
  }

  val (q, r) = BigInt(11) /% 3
  println("pattern in var decl: " + q + ", " + r)
  
  
}