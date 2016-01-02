package bs.scalaimpatient

object Chapter10TraitsScratch extends App {
  
  // trait order
  println("trait order")
  trait Named { def name: String }
  trait ThingOne extends Named {
    override val name = "one"
  }
  trait ThingTwo extends Named {
    override val name = "two"
  }
  class TheThing extends ThingOne with ThingTwo {
    override def toString = { "Thing, name: " + name }
  }
  println(new TheThing) // prints 'Thing, name: two'
  
  // ceasar cypher impl trial
  println("ceasar cypher impl trail")
  val chars = ' ' to '~'
  println(chars.toList)
  val key = 3
  val encryptedindexd = (chars.indexOf('d') + key )% chars.size
  println('d' + " becomes: " + chars(encryptedindexd))

  val encryptedindextild = (chars.indexOf('~') + key )% chars.size
  println('~' + " becomes: " + chars(encryptedindextild))
  
  // map string chars trial
  val res = "asdf".map(_.toUpper)
  println("result: ("+res.getClass+")" + res )
  
}