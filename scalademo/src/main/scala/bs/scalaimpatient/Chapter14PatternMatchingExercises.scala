package bs.scalaimpatient

object Chapter14PatternMatchingExercises extends App {
  // 2. swap two
  println("2. swap two")
  def swaptwo(in: (Int, Int)) = {
    in match {
      case (l, r) => (r, l)
      case _      => (0, 0)
    }
  }
  println { swaptwo((1, 2)) }

  // 3. swap first pair in array
  println("3. swap first pair in array")
  def swapfirstpairv1(in: Array[Int]) = {
    in match {
      case Array(l, r, _*) => r +: l +: in.tail.tail
      case _               => Array()
    }
  }
  println { "v1: " + swapfirstpairv1(Array(1, 2, 3, 4, 5)).toList }

  def swapfirstpairv2(in: Array[Int]) = {
    in match {
      case Array(l, r, rest @ _*) => Array(r, l) ++ rest
      case _                      => Array()
    }
  }
  println { "v2: " + swapfirstpairv2(Array(5, 4, 3, 2, 1)).toList }

  // 4. multiple case class
  println("4. multiple case class")
  abstract class Item { def price(): Int }
  case class Article(description: String, price: Int) extends Item
  case class Multiple(itemCount: Int, item: Item) extends Item {
    override def price() = itemCount * item.price
  }

  val multToasters = Multiple(5, Article("Toaster", 20))
  println { multToasters + ": price=" + multToasters.price }

  // 5. leaf valued tree
  println("5. leaf valued tree")
  def leafSum(tree: List[Any]): Int = {
    tree map {
      _ match {
        case l: List[Any] => leafSum(l)
        case i: Int       => i
        case _            => 0
      }
    } sum
  }
  val tree: List[Any] = List(List(3, 8), 2, List(5))
  println { leafSum(tree) }

  // 6. binary tree
  println("6. binary tree")
  sealed abstract class BinaryTree
  case class Leaf(value: Int) extends BinaryTree
  case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree

  def sumTreev1(tree: BinaryTree): Int = {
    tree match {
      case l: Leaf => l.value
      case n: Node => sumTreev1(n.left) + sumTreev1(n.right)
      case _       => 0
    }
  }

  val bintree = Node(Node(Leaf(3), Leaf(8)), Node(Leaf(2), Leaf(5)))
  println { sumTreev1(bintree) }

  def sumTreev2(tree: BinaryTree): Int = {
    tree match {
      case Leaf(v)    => v
      case Node(l, r) => sumTreev2(l) + sumTreev2(r)
      case _          => 0
    }
  }
  println { sumTreev2(bintree) }
  
  // 7. 

}