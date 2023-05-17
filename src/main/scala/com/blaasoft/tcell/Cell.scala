package com.blaasoft.tcell

import java.util.Timer
import java.util.TimerTask

trait ExternalObserver[T] {
  protected var currentValueHasChanged = false
  protected var currentValue: T = _
  private var externalObservers = Set[T => Unit]()

  final def observe(f: T => Unit): Unit = externalObservers += f

  final def notifyObservers(): Unit =
    if currentValueHasChanged then externalObservers.foreach(_(currentValue))
    currentValueHasChanged = false
}

trait Cell[+T] extends Node:
  def apply(): Cell.Observed[T]

  def observe(f: T => Unit): Unit

object Cell:
  type Observed[T] = AbstractCell[?] ?=> T

  def caller(using o: AbstractCell[?]) = o

  def apply[T](expr: Observed[T]) =
    new AbstractCell[T]:
      val eval = expr
      computeValue()

  abstract class AbstractCell[T] extends Cell[T] with ExternalObserver[T]:
    final def apply(): Observed[T] =
      Node.connect(this, caller)
      currentValue

    protected def eval: Observed[T]

    def computeValue(): Unit =
      val newValue = eval(using this)
      if newValue != currentValue then
        currentValue = newValue
        currentValueHasChanged = true

    override def toString(): String = currentValue.toString()

  end AbstractCell


  abstract class DebouncedCell[T] extends AbstractCell[T]:

    override def computeValue(): Unit = super.computeValue()

  end DebouncedCell


  class Var[T](initialExpr: Observed[T]) extends AbstractCell[T]:
    protected var eval = initialExpr
    computeValue()

    def update(newExpr: Observed[T])(using transaction: CellTransaction) =
      transaction.addCellToTransaction(this)
      eval = newExpr
      computeValue()

      transaction match
        case imp if imp == ImplicitCellTransaction => ImplicitCellTransaction.propagate(this)
        case _ =>

  end Var

  class DebouncedVar[T](initialExpr: Observed[T]) extends AbstractCell[T]:
    protected var eval = initialExpr
    computeValue()

    private var timerIsStarted = false

    def update(newExpr: Observed[T])(using transaction: CellTransaction): Unit =

      eval = newExpr

      if (!timerIsStarted) {
        timerIsStarted = true
        new Timer().schedule(new TimerTask {

          override def run(): Unit = {
            timerIsStarted = false
            computeValue()
            ImplicitCellTransaction.propagate(DebouncedVar.this)
          }

        }, 1000)
      }


  end DebouncedVar

  given noObserver: AbstractCell[?] = new AbstractCell[Nothing]:
    override def eval = ???

    override def computeValue(): Unit = {}

end Cell

extension (cell: Cell[Int])
  def +(other: Cell[Int]): Cell[Int] =
    Cell {
      cell() + other()
    }



//////////////

def threshold(cell: Cell[Double], delta: Double = 1.0): Cell[Double] =
  new Cell.AbstractCell[Double]:
    currentValue = Math.round(cell())
    val eval =
      val aValue = cell()
      if aValue > currentValue then aValue.floor else aValue.ceil
    computeValue()