import com.blaasoft.tcell.managers.CellTransactionManager
import com.blaasoft.tcell.managers.CellTransactionManager.TestCellTransactionManager
import com.blaasoft.tcell.{+, Cell, CellTransaction, ImplicitCellTransaction}

import scala.collection.mutable.ListBuffer

class TestCellTransactionManagerTest extends munit.FunSuite {

  given CellTransaction = {
    val transaction = new ImplicitCellTransaction
    transaction
  }

  test("Test b = a + 1, then update a") {
    val a = Cell.Var(10)
    val b = Cell {
      a() + 1
    }
    assertEquals(a(), 10)
    assertEquals(b(), 11)

    val manager = CellTransactionManager.TestCellTransactionManager {
      a() = 20
    }

    manager.setTime(1)

    assertEquals(a(), 20)
    assertEquals(b(), 21)
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
    val m = CellTransactionManager.TestCellTransactionManager {
      x() = 5
      y() = 15
    }

    m.setTime(2)

    assertEquals(signal.toList, List(10))
  }
}
