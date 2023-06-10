package com.blaasoft.tcell

trait CellTransactionManager:
  def provideTransaction(): CellTransaction

object CellTransactionManager:

  def apply(body: CellTransaction ?=> Unit) = {
    val k = DefaultCellTransaction(body)
    k.doPropagate()
    k
  }

end CellTransactionManager
