
package com.blaasoft.example

import com.blaasoft.tcell._

@main def hello: Unit =

  // Test Nodes
  val a = Node("a")
  val b = Node("b")
  val c = Node("c")
  val d = Node("d")
  val e = Node("e")
  val f = Node("f")
  val g = Node("g")

  Node.connect(a, b)
  Node.connect(a, c)
  Node.connect(b, d)
  Node.connect(d, e)
  Node.connect(c, e)
  Node.connect(d, f)
  Node.connect(e, f)
  Node.connect(g, f)

  println(s"a = ${a.closureWithMaxDepth()}")
  println(s"g = ${g.closureWithMaxDepth()}")

  println(s"p = ${Node.nodesByDepth(List(a, g))}")

  println("----------------------------------")


  val ax = Cell.Var(10)
  ax.observe { newValue =>
    println(s"Value of ax = $newValue")
  }
  println(ax())

  val bx = Cell {
    ax() + 1
  }
  bx.observe { newValue =>
    println(s"Value of bx = $newValue")
  }

  CellTransaction {
    ax() = 11
  }

  val list = Cell.Var(List.empty[Int])

  val list2 = Cell.Var {
    
  }


  println(list)

  /*
  val b1 = BankAccount()
  val b2 = BankAccount()

  val cons = consolidated(List(b1, b2))

  cons.addObserver {
    newValue => println(newValue)
  }

  CellTransaction {
    b1.deposit(250)
    print("before commit")
  }

  CellTransaction {
    b2.deposit(250)
  }
  */


  //println(s"b1 = ${b1.balance()}")

  /*
  var quantity: Cell.Var[Int] = null
  var price: Cell.Var[Int] = null
  var priceQuantity: Cell[Int] = null

  CellTransaction {
    quantity = Cell.Var(1000)
    price = Cell.Var(2)
    
    priceQuantity = Cell {
      quantity.apply2() * price.apply2()
    }

    priceQuantity.addObserver((newValue) =>
      println(s"New PriceQuantity = $newValue")
    )
  }

  CellTransaction {
    price() = 4
  }

  CellTransaction {
    quantity() = 2000
    price() = 3
  }
*/
