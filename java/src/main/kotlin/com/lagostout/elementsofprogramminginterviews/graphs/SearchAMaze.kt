package com.lagostout.elementsofprogramminginterviews.graphs

import java.util.*

/**
 * Problem 19.1 page 360
 */
object SearchMaze {

    data class Result(val path: List<Point>, val graph: Map<Point, Set<Point>>)
    data class Frame(val point: Point, val adjacentPoints: Iterator<Point>)

    fun findPathThroughMaze(
            grid: List<List<Boolean>>,
            entry: Point, exit: Point): Result {
        if (!containsOpenPixel(grid, entry) ||
                !containsOpenPixel(grid, exit)) return Result(emptyList(), emptyMap())
        val graph = toGraph(grid)
        val stackOfFrames = LinkedList<Frame>()
        val pixelsInPath = mutableSetOf<Point>()
        stackOfFrames.push(
                Frame(point = entry, adjacentPoints = graph[entry]!!.iterator()))
        pixelsInPath.add(entry)
        while (stackOfFrames.isNotEmpty() &&
                !(stackOfFrames.first().point == exit &&
                        stackOfFrames.last().point == entry)) {
            val (_, adjacentPoints) = stackOfFrames.peek()
            if (adjacentPoints.hasNext()) {
                val adjacentPoint = adjacentPoints.next()
                if (pixelsInPath.contains(adjacentPoint)) continue
                pixelsInPath.add(adjacentPoint)
                stackOfFrames.push(
                        Frame(point = adjacentPoint,
                                adjacentPoints = graph[adjacentPoint]!!.iterator()))
            } else {
                val pixelNotInPath  = stackOfFrames.pop().point
                pixelsInPath.remove(pixelNotInPath)
            }
        }
        return Result(stackOfFrames.map { it.point }.reversed(), graph)
    }

    private fun containsOpenPixel(grid: List<List<Boolean>>, point: Point): Boolean {
        return (point.row < grid.size && point.column < grid[0].size && grid[point.row][point.column])
    }

    private fun toGraph(grid: List<List<Boolean>>): Map<Point, Set<Point>> {
        val adjacencies: MutableMap<Point, MutableSet<Point>> = mutableMapOf()
        grid.forEachIndexed { rowIndex, list ->
            list.forEachIndexed {
                columnIndex, isOpen ->
                if (isOpen) {
                    val point = Point(columnIndex, rowIndex)
                    val adjacentPoints = mutableSetOf<Point>()
                    adjacencies.put(point, adjacentPoints)
                    val previousRow = rowIndex - 1
                    if (previousRow >= 0) {
                        val previousRowPoint = Point(columnIndex, previousRow)
                        if (adjacencies.containsKey(previousRowPoint)) {
                            adjacencies[point]?.add(previousRowPoint)
                            adjacencies[previousRowPoint]?.add(point)
                        }
                    }
                    val previousColumn = columnIndex - 1
                    if (previousColumn >= 0) {
                        val previousColumnPoint = Point(previousColumn, rowIndex)
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

}