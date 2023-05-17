import com.blaasoft.tcell.Cell
import com.blaasoft.tcell.CellTransaction
import com.blaasoft.tcell.ImplicitCellTransaction
import scala.collection.mutable.ListBuffer
import com.blaasoft.tcell.+
// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html


class MySuite extends munit.FunSuite {
  test("Simple init of a var") {
    val a = Cell.Var(33)
    assertEquals(a(), 33)
  }

  test("Test b = a + 1") {
    val a = Cell.Var(10)
    val b = Cell {
      a() + 1
    }
    assertEquals(a(), 10)
    assertEquals(b(), 11)
  }

  test("Test b = a + 1, then update a") {
    val a = Cell.Var(10)
    val b = Cell { a() + 1 }
    assertEquals(a(), 10)
    assertEquals(b(), 11)

    CellTransaction { 
      a() = 20
    }
    
    assertEquals(a(), 20)
    assertEquals(b(), 21)
  }

  test("Test b = a + 1, then update a") {

    val a = Cell.Var(10)
    val b = Cell { a() + 1 }

    a() = 11
    assertEquals(a(), 11)
    assertEquals(b(), 12)
  }

  test("Test middle without transaction") {

    val x = Cell.Var(0)
    val y = Cell.Var(10)
    var middle = Cell {
      (x() + y()) / 2
    }

    assertEquals(middle(), 5)

    var signal = ListBuffer[Int]()
    middle.observe(value => signal += value)

    // Translate 5 on the right
    x() = 5
    y() = 15

    println(signal)

    assertEquals(signal.toList, List(7, 10))
  }

  test("Test middle with transaction") {

    val x = Cell.Var(0)
    val y = Cell.Var(10)
    var middle = Cell {
      (x() + y()) / 2
    }

    assertEquals(middle(), 5)

    var signal = ListBuffer[Int]()
    middle.observe(value => signal += value)

    // Translate 5 on the right
    CellTransaction {
      x() = 5
      y() = 15
    }

    assertEquals(signal.toList, List(10))
  }

  test("Adding two cells") {
    val a = Cell.Var(1)
    val b = Cell.Var(2)

    val c = a + b

    assertEquals(c(), 3)
  }


  test("Adding three cells") {

    val a = Cell.Var(1)
    val b = Cell.Var(2)
    val c = Cell.Var(3)

    val sum = a + b + c

    var signal = ListBuffer[Int]()
    sum.observe(value => signal += value)

    assertEquals(sum(), 6)

    a() = 3

    assertEquals(sum(), 8)

    b() = 3

    assertEquals(sum(), 9)

    assertEquals(signal.toList, List(8, 9))
  }

  test("Mofifings a list of cells") {
    val a = Cell.Var("One")
    val b = Cell.Var("Two")
    val c = Cell.Var("Three")

    val list = Cell.Var(List(a, b, c))
    print(list())     // Outputs List("One", "Two", "Three")

    a() = "Uno"
    b() = "Dos"
    c() = "Tres"

    print(list())
  }


  test("gogogo") {
    val a = Cell.DebouncedVar(0.0)

    a.observe(newValue => println(newValue))

    for i <- 0 until 50
    do
      a() = i.doubleValue()
      Thread.sleep(100)

  }
}