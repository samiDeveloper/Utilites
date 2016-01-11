package bs.scalaimpatient

object Chapter12HigherOrderFunctionsScratch extends App {
  def mul(x: Int, y: Int) = x * y
  def mulOneAtATime(x: Int): (Int => Int) = (y: Int) => x * y
  println("mul: " + mul(2, 3))
  println("mulOneAtATime: " + mulOneAtATime(3)(4))

  def mulOneAtATimeShort(x: Int)(y: Int) = x * y
  println("mulOneAtATimeShort: " + mulOneAtATimeShort(4)(5))

  // corresponds
  class A {
    def corresponds[B](that: B)(p: (A, B) => Boolean) = p(this, that)
  }

  // could be written out as
  class C {
    def corresponds[B](that: B): (((C, B) => Boolean) => Boolean) = (p: (C, B) => Boolean) => p(this, that)
  }

  // control abstractions
  def runInThread(block: () => Unit) {
    new Thread() {
      override def run() { block() }
    }.start()
  }

  runInThread { () => println("Hi1"); Thread.sleep(1000); println("Bye1") }
  runInThread(() => { println("Hi2"); Thread.sleep(1000); println("Bye2") })

  // use call by name for procedures to omit the () => in the call
  def runInThreadCBN(block: => Unit) {
    new Thread {
      override def run() { block }
    }.start()
  }

  runInThreadCBN { println("Hi CBN"); Thread.sleep(1000); println("Bye CBN") }
}