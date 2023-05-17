package com.blaasoft.tcell

import com.blaasoft.tcell.Node.connect

object Node:
    def apply(name: String): Node =
        NamedNode(name)

    def connect(callee: Node, caller: Node): Unit =
        callee.addCaller(caller)
        caller.addCallee(callee)

    def nodesByDepth(nodes: List[Node]): List[(Int, List[Node])] =
        val k = closureWithMaxDepth(nodes).groupBy((node, depth) => depth) 
        val t = k.map(t => (t._1, t._2.keys.toList)).toList
        t

    def closureWithMaxDepth(nodes: List[Node]): Map[Node, Int] =
        val allPaths = nodes.flatMap { node => node.closure() }
        nodeDepths(allPaths)

    private def nodeDepths(paths: List[List[Node]]): Map[Node, Int] =
        paths.flatMap(_.zipWithIndex).groupMapReduce(_._1)(t => t._2)(Math.max(_, _))

trait Node:
    type Path = List[Node]

    private var callees = Set[Node]()
    private var callers = Set[Node]()

    private def addCallee(callee: Node): Unit =
        callees += callee

    private def addCaller(caller: Node): Unit =
        callers += caller

    def closure(): List[Path] =
        if(callers.isEmpty) then List(List(this))
        else
            val all = callers.flatMap(_.closure()).toList
            all.map(path => this :: path)

    def closureWithMaxDepth(): Map[Node, Int] =
        Node.nodeDepths(closure())

trait RedNode

class NamedNode(name: String) extends Node:
    override def toString(): String = name

extension (c: Node)
  def --> (caller: Node): Unit = connect(c, caller)
    