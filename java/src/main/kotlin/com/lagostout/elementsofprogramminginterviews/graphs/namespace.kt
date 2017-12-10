package com.lagostout.elementsofprogramminginterviews.graphs

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

data class GraphNode<T>(val value: T,
                        val adjacentNodes: MutableList<GraphNode<T>> = mutableListOf()) {
    private val id: Int = ID++

    override fun equals(other: Any?): Boolean {
        if (other !is GraphNode<*>) {
            return false
        }
        if (this === other) {
            return true
        }
        return EqualsBuilder().append(
                this.id, other.id).isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
                .append(this.id)
                .toHashCode()
    }

    override fun toString(): String {
        return "GraphNode(value=$value, adjacentNodes=${adjacentNodes.map { it.value }}, id=$id)"
    }

    companion object {
        var ID = 0
    }
}

data class RawGraphNode<out T>(val value: T,
                               val adjacentNodes: List<Int> = emptyList())

/**
 * Creates an undirected graph represented as a digraph with edges
 * in both directions between adjacent vertices.
 */
fun <T> toGraph(rawNodes: List<RawGraphNode<T>>): List<GraphNode<T>> {
    return toDigraph(rawNodes).apply {
        if (isEmpty()) return@apply
        val nodesIterator = iterator()
        val exploredNodes = mutableSetOf<GraphNode<T>>()
        val nodes = mutableSetOf<GraphNode<T>>()
        while (true) {
            if (nodes.isEmpty()) {
                if (!nodesIterator.hasNext()) break
                val node = nodesIterator.next()
                if (exploredNodes.contains(node)) continue
                nodes.add(node)
            }
            val nextNodes = mutableSetOf<GraphNode<T>>()
            nodes.forEach { source ->
                exploredNodes.add(source)
                nextNodes.addAll(source.adjacentNodes
                        .filter { it !in exploredNodes })
                source.adjacentNodes.forEach { destination ->
                    destination.adjacentNodes.add(source)
                }
            }
            nodes.apply { clear() }.addAll(nextNodes)
        }
    }
}

fun <T> toDigraph(rawNodes: List<RawGraphNode<T>>):
        List<GraphNode<T>> {
    val digraph = mutableListOf<GraphNode<T>>()
    rawNodes.forEach {
        digraph.add(GraphNode(it.value))
    }
    digraph.forEachIndexed { index, node ->
        rawNodes[index].adjacentNodes.forEach {
            node.adjacentNodes.add(digraph[it])
        }
    }
    return digraph
}

/**
 * Transforms a boolean matrix into an adjacency map of a graph.
 * Edges are 2-way.
 */
fun toGraph(booleanMatrix: List<List<Boolean>>):
        MutableMap<Point<Boolean>, MutableSet<Point<Boolean>>> {
    val adjacencies1 = toGraph(booleanMatrix, true)
    val adjacencies2 = toGraph(booleanMatrix, false)
    return (adjacencies1 + adjacencies2).toMutableMap()
}

/**
 * Transforms a boolean matrix into an adjacency map of a graph.
 * The map will only contain adjacencies for cells with the value
 * specified by <code>cellValueToRetain</code>.  Edges are 2-way.
 */
fun toGraph(booleanMatrix: List<List<Boolean>>,
            cellValueToRetain: Boolean = true):
        MutableMap<Point<Boolean>, MutableSet<Point<Boolean>>> {
    val adjacencies: MutableMap<Point<Boolean>, MutableSet<Point<Boolean>>> =
            mutableMapOf()
    booleanMatrix.forEachIndexed { rowIndex, list ->
        list.forEachIndexed {
            columnIndex, cellValue ->
            if (cellValue == cellValueToRetain) {
                val point = Point(columnIndex, rowIndex, cellValue)
                val adjacentPoints = mutableSetOf<Point<Boolean>>()
                adjacencies.put(point, adjacentPoints)
                val previousRow = rowIndex - 1
                if (previousRow >= 0) {
                    val previousRowPoint = Point(columnIndex, previousRow, cellValue)
                    if (adjacencies.containsKey(previousRowPoint)) {
                        adjacencies[point]?.add(previousRowPoint)
                        adjacencies[previousRowPoint]?.add(point)
                    }
                }
                val previousColumn = columnIndex - 1
                if (previousColumn >= 0) {
                    val previousColumnPoint = Point(previousColumn, rowIndex, cellValue)
                    if (adjacencies.containsKey(previousColumnPoint)) {
                        adjacencies[point]?.add(previousColumnPoint)
                        adjacencies[previousColumnPoint]?.add(point)
                    }
                }
            }
        }
    }
    return adjacencies
}

/**
 * Compute components of a graph.
 */
fun computeComponents(graph: Map<Point<Boolean>, Set<Point<Boolean>>>):
        Set<Set<Point<Boolean>>> {
    // TODO Modify to also group components by value i.e. T/F
    val visited = mutableSetOf<Point<Boolean>>()
    val components = mutableSetOf<Set<Point<Boolean>>>()
    graph.keys.forEach { vertex ->
        if (visited.contains(vertex)) return@forEach
        var verticesToVisit = mutableSetOf(vertex)
        var vertexCount = 0
        val currentRegion = mutableSetOf(vertex)
        while (verticesToVisit.isNotEmpty()) {
            vertexCount += verticesToVisit.size
            visited.addAll(verticesToVisit)
            verticesToVisit = verticesToVisit.fold(mutableSetOf()) {
                acc, vertex ->
                graph[vertex]?.let {
                    adjacentVertices ->
                    acc.apply {
                        addAll(adjacentVertices.filterNot { visited.contains(it) })
                    }
                } ?: acc
            }
            currentRegion.addAll(verticesToVisit)
        }
        components.add(currentRegion)
    }
    return components
}

