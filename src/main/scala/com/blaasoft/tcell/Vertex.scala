package com.blaasoft.tcell

import com.blaasoft.tcell.Vertex.connect

trait Vertex:
    type Path = List[Vertex]

    private var predecessors = Set[Vertex]()
    private var successors = Set[Vertex]()

    private def addDirectPredecessor(callee: Vertex): Unit =
        predecessors += callee

    private def addDirectSuccessor(caller: Vertex): Unit =
        successors += caller

    def findAllMaximumPaths(): List[Path] =
        if(successors.isEmpty) then List(List(this))
        else
            val all = successors.flatMap(_.findAllMaximumPaths()).toList
            all.map(path => this :: path)

    def closureWithMaxDepth(): Map[Vertex, Int] =
        Vertex.nodeDepths(findAllMaximumPaths())

end Vertex

object Vertex:
    def apply(name: String, break: Boolean = false): Vertex =
        if !break then NamedNode(name) else new NamedNode(name) with Break

    def connect(from: Vertex, to: Vertex): Unit =
        from.addDirectSuccessor(to)
        to.addDirectPredecessor(from)

    def nodesByDepth(nodes: List[Vertex]): List[(Int, List[Vertex])] =
        val k = closureWithMaxDepth(nodes).groupBy((node, depth) => depth)
        val t = k.map(t => (t._1, t._2.keys.toList)).toList
        t

    def closureWithMaxDepth(nodes: List[Vertex]): Map[Vertex, Int] =
        val allPaths = nodes.flatMap { node => node.findAllMaximumPaths() }
        nodeDepths(allPaths)

    // Convenient method when initial node is a singleton
    def closureWithMaxDepth(node: Vertex): Map[Vertex, Int] = closureWithMaxDepth(List(node))

    private def nodeDepths(paths: List[List[Vertex]]): Map[Vertex, Int] =
        paths.flatMap(_.zipWithIndex).groupMapReduce(_._1)(t => t._2)(Math.max(_, _))

    trait Break

end Vertex

class NamedNode(name: String) extends Vertex:
    override def toString(): String = name

extension (c: Vertex)
  def --> (caller: Vertex): Unit = connect(c, caller)
    