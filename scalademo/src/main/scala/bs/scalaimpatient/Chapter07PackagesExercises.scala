package bs.scalaimpatient

// run me
object Chapter07PackagesExercise01Main extends App {
  // 1. chained vs nested
  println("chained vs nested")
  _root_.com.horstmann.impatient.Chapter07PackagesExercise01Chained.main(Array())
  _root_.com.horstmann.impatient.Chapter07PackagesExercise01Nested.main(Array())

  // 2. com package puzzler
  println("2. com package puzzler")
  val editor = new com.sun.beans.editors.ColorEditor()

  // 3. package functions
  random.setSeed(3)
  println("3. package functions, random: " + random.nextDouble() + " " + random.nextInt() + " " + random.nextDouble() + " " + random.nextInt())

  // 6. rename java scala hashmaps
  import java.util.{ HashMap => JavaHashMap }
  import scala.collection.mutable.{ HashMap => ScalaHashMap }

  val javaMap = new JavaHashMap[String, Int]()
  javaMap.put("a", 1)
  javaMap.put("b", 2)

  val scalaMap = new ScalaHashMap[String, Int]

  val javaEntries = javaMap.entrySet().iterator()
  while (javaEntries.hasNext()) {
    val javaEntry = javaEntries.next;
    scalaMap.put(javaEntry.getKey, javaEntry.getValue)
  }
  println("rename java scala hashmaps: " + scalaMap)

  // 9. import java system
  println("9. import java system")
  import java.lang.System
  val username = System.getProperty("user.name")
  print("Give password: ")
  val scanner = new java.util.Scanner(System.in) // System.console() returns null inside eclipse so we cheat a little here
  val password = scanner.nextLine
  if (password equals ("secret")) System.out.println("Greetings")
  else System.err.println("Wrong password")
  
  // 10. scala overrides java.lang
  // Boolean, Byte, Character, Double, Float, Long, Short
}

// @ 1. chained vs nested...
package _root_.com.horstmann.impatient {
  object Chapter07PackagesExercise01Chained extends App {
    _root_.com.horstmann.Exercise0701Demo.demo(this.getClass.toString()) // need to qualify Exercise0701Demo
  }
}

package _root_.com {
  package horstmann {
    package impatient {
      object Chapter07PackagesExercise01Nested extends App {
        Exercise0701Demo.demo(this.getClass.toString()) // no need to qualify, Exercise0701Demo is in scope
      }
    }
  }
}

package _root_.com.horstmann {
  object Exercise0701Demo {
    def demo(s: String) {
      println("Exercise0701Demo for '" + s + "'")
    }
  }
}

// @ 2. com package puzzler
package com.sun.beans.editors {
  class ColorEditor {
    println("ColorEditor crash!")
  }
}
