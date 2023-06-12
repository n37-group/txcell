package com.blaasoft.tcell.managers

import com.blaasoft.tcell.{CellTransaction, DefaultCellTransaction}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait CellTransactionManager

object CellTransactionManager:

  class Simple(body: CellTransaction ?=> Unit) extends CellTransactionManager:
    val transaction = DefaultCellTransaction(body)
    transaction.doPropagate()
  end Simple

  class TestCellTransactionManager(body: CellTransaction ?=> Unit) extends CellTransactionManager:
    private var now: Long = 0
    private val tasks: mutable.SortedMap[Long, ListBuffer[CellTransaction]] = mutable.SortedMap[Long, ListBuffer[CellTransaction]]()

    private val transaction = DefaultCellTransaction(body)
    addTask(now, transaction)

    def setTime(newTime: Long): Unit =
      now = newTime
      run()

    private def addTask(when: Long, task: CellTransaction): Boolean =
      if when >= now then
        tasks.get(when) match
          case Some(list) => list += task
          case None => tasks.addOne(when -> ListBuffer(task))
        true
      else
        false

    def run(): Unit =
      val iterator = tasks.iterator
      while iterator.hasNext do
        val (time, transactions) = iterator.next()
        if time <= now then
          transactions.foreach(t => t.doPropagate())
    end run

  end TestCellTransactionManager

end CellTransactionManager
