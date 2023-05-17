import com.blaasoft.tcell.Cell
import com.blaasoft.tcell.CellTransaction
import com.blaasoft.tcell.ImplicitCellTransaction
import scala.collection.mutable.ListBuffer
import com.blaasoft.tcell.+
import com.blaasoft.tcell.Node
import com.blaasoft.tcell.Node.closureWithMaxDepth
import com.blaasoft.tcell.-->
import com.blaasoft.tcell.Node.nodesByDepth


class NodesTest extends munit.FunSuite {
  test("Simple test of nodes") {
    val a = Node("a")
    val b = Node("b")
    val c = Node("c")
    val d = Node("d")
    val e = Node("e")
    val f = Node("f")
    val g = Node("g")
    val h = Node("h")

    a --> c
    b --> d
    b --> e
    d --> f
    e --> g
    c --> h
    f --> g
    f --> h
    g --> h

    val initialNodes = List(a, b)

    val map = closureWithMaxDepth(initialNodes)

    println(map)

    println(nodesByDepth(initialNodes))
    
  }

  test("Simple test of nodes 2") {
    val a = Node("a")
    val b = Node("b")
    val c = Node("c")
    val d = Node("d")
    val e = Node("e")
    val f = Node("f")
    val g = Node("g")
    val h = Node("h")

    a --> c
    b --> d
    b --> e
    d --> f
    e --> g
   // c --> h
    f --> g
    f --> h
    g --> h

    val initialNodes = List(a, b)

    val map = closureWithMaxDepth(initialNodes)

    println(map)

    println(nodesByDepth(initialNodes))
    
  }
}

