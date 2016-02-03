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
  println("6. binary tree");
  {
    sealed abstract class BinaryTree
    case class Leaf(value: Int) extends BinaryTree
    case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree

    def leafSum(tree: BinaryTree): Int = {
      tree match {
        case l: Leaf => l.value
        case n: Node => leafSum(n.left) + leafSum(n.right)
        case _       => 0
      }
    }

    val bintree = Node(Node(Leaf(3), Leaf(8)), Node(Leaf(2), Leaf(5)))
    println { leafSum(bintree) }

    def leafSumv2(tree: BinaryTree): Int = {
      tree match {
        case Leaf(v)    => v
        case Node(l, r) => leafSumv2(l) + leafSumv2(r)
        case _          => 0
      }
    }
    println { leafSumv2(bintree) }
  }

  // 7. bintree children unlimited
  println("7. bintree children unlimited");
  {
    sealed abstract class BinaryTree
    case class Leaf(value: Int) extends BinaryTree
    case class Node(children: BinaryTree*) extends BinaryTree

    def leafSum(tree: BinaryTree): Int = {
      tree match {
        case Leaf(v)           => v
        case Node(childs @ _*) => childs.map(leafSum(_)).foldLeft(0)(_ + _)
        case _                 => 0
      }
    }

    val bintree = Node(Node(Leaf(3), Leaf(8)), Leaf(2), Node(Leaf(5)))
    println { leafSum(bintree) }
  }

  // 8. bintree using operator
  println("8. bintree using operator");
  {
    sealed abstract class BinaryTree
    case class Leaf(value: Int) extends BinaryTree
    case class Node(operator: Char, children: BinaryTree*) extends BinaryTree

    def eval(tree: BinaryTree): Int = {
      tree match {
        case Leaf(v) => v
        case Node(op, childs @ _*) => op match {
          case '+' => childs.foldLeft(0)(_ + eval(_))
          case '-' => childs.foldLeft(0)(_ - eval(_))
          case '*' => childs.foldLeft(1)(_ * eval(_))
          case _   => 1
        }
      }
    }

    val opTree = Node('+', Node('*', Leaf(3), Leaf(8)), Leaf(2), Node('-', Leaf(5)))
    println { eval(opTree) }

    // 9. compute sum of non-nodes
    println("9. compute sum of non-nodes");

    def leafSum(tree: BinaryTree): Int = {
      if (tree.isInstanceOf[Node])
        tree.asInstanceOf[Node].children.foldLeft(0)(_ + leafSum(_))
      else
        tree.asInstanceOf[Leaf].value
    }

    println(leafSum(opTree))
  }

  // 10. compose functions
  println("10. compose functions")
  type T = Double => Option[Double]
  def compose(l: T, r: T): T = {
    (x: Double) => r(x).flatMap(l(_)) // must apply r first to get to the results as in the example
  }

  def g(x: Double) = if (x != 1) Some(1 / (x - 1)) else None
  def f(x: Double) = if (x >= 0) Some(math.sqrt(x)) else None
  def h = compose(f, g)

  println(h(2)) // Some(1.0)
  println(h(1)) // None
  println(h(0)) // None
  println(h(-1)) // None

}