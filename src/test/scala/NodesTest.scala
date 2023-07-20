import com.blaasoft.tcell.Cell
import com.blaasoft.tcell.CellTransaction
import com.blaasoft.tcell.ImplicitCellTransaction
import scala.collection.mutable.ListBuffer
import com.blaasoft.tcell.+
import com.blaasoft.tcell.Vertex
import com.blaasoft.tcell.Vertex.closureWithMaxDepth
import com.blaasoft.tcell.-->
import com.blaasoft.tcell.Vertex.nodesByDepth


class NodesTest extends munit.FunSuite {

  test("Three nodes") {
    val a = Vertex("a")
    val b = Vertex("b")
    val c = Vertex("c")

    a --> b
    b --> c

    val map: Map[Vertex, Int] = closureWithMaxDepth(a)

    assertEquals(map, Map(a -> 0, b -> 1, c -> 2))
  }

  test("Three nodes but the second node is a Break node") {
    val a = Vertex("a")
    val b = Vertex("b", true)
    val c = Vertex("c")

    a --> b
    b --> c

    val map: Map[Vertex, Int] = closureWithMaxDepth(a)

    assertEquals(map, Map(a -> 0, b -> 1, c -> 2))
  }

  test("Simple test of nodes") {
    val a = Vertex("a")
    val b = Vertex("b")
    val c = Vertex("c")
    val d = Vertex("d")
    val e = Vertex("e")
    val f = Vertex("f")
    val g = Vertex("g")
    val h = Vertex("h")

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
    val a = Vertex("a")
    val b = Vertex("b")
    val c = Vertex("c")
    val d = Vertex("d")
    val e = Vertex("e")
    val f = Vertex("f")
    val g = Vertex("g")
    val h = Vertex("h")

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

