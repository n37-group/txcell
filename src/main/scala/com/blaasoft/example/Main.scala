package com.blaasoft.example

import com.blaasoft.tcell.{Cell, CellTransaction, ImplicitCellTransaction}
import com.blaasoft.tcell.managers.CellTransactionManager._

class Main
  @main def hello() =
    val a = Cell.Var(10)
    val b = Cell.Var(10)

    val c = Cell {
      a() + b()
    }

    // This line of code will set the new vlue of a to 30.
    // Since it is not enclosed the implicit transaction will be used.
    // If You just modify one cell at a time, you don't need to create explicitly a new transaction.
    a() = 20

    println(s"c = $c")
    // Will print c = 30

    // Let's say you want to modify a and b at the same time.
    // In that case you need
    tx {
      a() = 100
      b() = 1

      // This transaction is not committed yet.
      // Will print, c = 30
      println(s"c = $c")
    }
    // The transaction is cmmitted.
    // Will print c = 101
    println(s"c = $c")