package com.blaasoft.tcell

import com.blaasoft.tcell.CellTransaction.propagate

trait CellTransaction:
  def addCellToTransaction(cell: Cell[?]): Unit

object CellTransaction:
  def apply(body: CellTransaction ?=> Unit) = DefaultCellTransaction(body)

  def propagate(initCells: List[Cell[?]]): Unit = {
    val paths = Node.nodesByDepth(initCells)

    paths.map(_._2).foreach(listOfNodes =>
      listOfNodes.map(x => x.asInstanceOf[Cell.AbstractCell[_]]).foreach(_.computeValue()))

    paths
      .flatMap(_._2)
      .map(_.asInstanceOf[Cell.AbstractCell[?]])
      .foreach(x => x.notifyObservers())
  }

  given noTransaction: CellTransaction = ImplicitCellTransaction

end CellTransaction


class DefaultCellTransaction(body: CellTransaction ?=> Unit) extends CellTransaction:
  private var modifiedCells:Set[Cell[?]] = Set.empty

  def addCellToTransaction(cell: Cell[?]): Unit = modifiedCells += cell

  implicit val T:CellTransaction = this

  body

  propagate(modifiedCells.toList)

end DefaultCellTransaction

object ImplicitCellTransaction extends CellTransaction:
  def addCellToTransaction(cell: Cell[?]): Unit = {}

  def propagate(from: Cell[?]): Unit =
    CellTransaction.propagate(List(from))

end ImplicitCellTransaction


