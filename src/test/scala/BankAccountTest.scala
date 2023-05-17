import com.blaasoft.tcell.Cell
import com.blaasoft.tcell.Cell.AbstractCell
import com.blaasoft.tcell.CellTransaction

import scala.collection.mutable.ListBuffer


class BankAccount:
    val balance = Cell.Var(0)

    def deposit(amount: Int)(using trans:CellTransaction): Unit =
        val currentBalance = balance()
        balance() = currentBalance + amount

end BankAccount

def consolidated(accts: List[BankAccount]) =
    Cell {accts.map(_.balance()).sum} 

class BankAccountTest extends munit.FunSuite {
    test("Ban account test") {
        val a = BankAccount()
        val b = BankAccount()

        val list = List(a, b)
        val total = consolidated(list)

        val observed = ListBuffer[Int]()

        total.observe(newValue => observed += newValue)

        a.deposit(100)      // Total is 100
        a.deposit(10)       // Total is 110
        b.deposit(40)       // Total is 150

        CellTransaction {
            a.deposit(20)   // Total is still 150
            a.deposit(30)   // Total is still 150
        }
        // Total is 200
        
        assertEquals(observed.toList, List(100, 110, 150, 200))
    }

    test("Threshold test") {
        val a = Cell.Var(10.5)

        val t = new AbstractCell[Option[Double]]:
            currentValue = None
            val eval = Some(currentValue match
                case None => Math.round(a())
                case Some(current) =>
                    val aValue = a()
                    if aValue > current then aValue.floor else aValue.ceil)
            computeValue()

        t.observe(ttt => println(ttt))

        a() = 13.5
        a() = 14.5
        a() = 13.5
        a() = 10.5


    }
}
