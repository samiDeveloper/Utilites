package bs.scalaimpatient

object Hello {
  def main(args: Array[String]) {
    println("Hello World!")
  }
}

object Colors extends Enumeration {
  val RED, GREEN, BLUE = Value
}

object TrafficLightColor extends Enumeration {
  val RED = Value("Stop")
  val YELLOW = Value("Brake")
  val GREEN = Value("Go")
}

object Chapter06Scratch extends App {
  val mycolor = Colors.BLUE;
  if (mycolor == Colors.GREEN) println("green") else println("no green")

  val mytrafficcolor: TrafficLightColor.Value = TrafficLightColor.RED
  println("mytrafficcolor: " + mytrafficcolor.id + " " + mytrafficcolor)
  println(TrafficLightColor.values)
  
  

}