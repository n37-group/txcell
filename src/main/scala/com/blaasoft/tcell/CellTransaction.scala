package com.blaasoft.tcell

import com.blaasoft.tcell.CellTransaction.propagate

trait CellTransaction:
  def addCellToTransaction(cell: Cell[?]): Unit

  def doPropagate(): Unit

object CellTransaction:

  given CellTransaction = {
    val transaction = new ImplicitCellTransaction
    transaction
  }

  def propagate(initCells: List[Cell[?]]): Unit = {
    val paths = Vertex.nodesByDepth(initCells)

    paths.map(_._2).foreach(listOfNodes =>
      listOfNodes.map(x => x.asInstanceOf[Cell.AbstractCell[_]]).foreach(_.computeValue()))

    paths
      .flatMap(_._2)
      .map(_.asInstanceOf[Cell.AbstractCell[?]])
      .foreach(x => x.notifyObservers())
  }

end CellTransaction


class DefaultCellTransaction(body: CellTransaction ?=> Unit) extends CellTransaction:
  private var modifiedCells:Set[Cell[?]] = Set.empty

  def addCellToTransaction(cell: Cell[?]): Unit = modifiedCells += cell

  def doPropagate(): Unit = propagate(modifiedCells.toList)

  implicit val T:CellTransaction = this

  body

  def maybeClose(): Unit =  {}

end DefaultCellTransaction

class ImplicitCellTransaction extends CellTransaction:
  def addCellToTransaction(cell: Cell[?]): Unit = {}

  def propagate(from: Cell[?]): Unit = CellTransaction.propagate(List(from))

  def doPropagate(): Unit = {}

end ImplicitCellTransaction


