// 'chained package clause' using '.', must use explicit import statement to resolve BsScopedUtil
package bs.scalaimpatient {
  object Chapter07PackagesScratch extends App {

    import bs.BsScopedUtil // imports can be anywhere 

    BsScopedUtil.demo()
  }
}

// 'nested packages', no need to import BsScopedUtil because members of the enclosing packages are visible
package bs {
  package scalaimpatient {
    object Scratch2 extends App {
      BsScopedUtil.demo()

      println(scratchprefix + " <-- from package object")
    }
  }
}

package bs {

  object BsScopedUtil extends App {
    def demo() { println("BsScopedUtil.demo3") }
  }
}

// import demos
object ImportDemoSelectorRename extends App {

	import java.awt.{ Color, Font } // selector
	import java.util.{ HashMap => JavaHashMap }  // rename

	val javahashmap = new JavaHashMap()
}

object ImportDemoSelectorHide extends App {

	import java.util.{ HashMap => _, _ }  // hide HashMap, import everything else
	import scala.collection.mutable._  // now HashMap unambiguously refers to the scala variant
}

