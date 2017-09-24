package com.lagostout.elementsofprogramminginterviews.graphs

import com.lagostout.common.nextInt
import com.lagostout.common.nextLevel
import com.lagostout.elementsofprogramminginterviews.graphs.PaintBooleanMatrix.flipRegionColor
import org.apache.commons.math3.random.RandomDataGenerator
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

class PaintBooleanMatrixSpek : Spek({
    describe("flipRegionColor") {
        testCases.forEach {
            (grid, start, expectedUnflippedPoints) ->
            given("grid $grid start $start") {
                it("flips color of region containing start point") {
                    assertEquals(expectedUnflippedPoints,
                            pointsWithColor(
                                    flipRegionColor(grid, start),
                                    grid[start.row][start.column]))
                }
            }
        }
    }
}) {
    companion object {

        /**
         * Filters points with color
         */
        fun pointsWithColor(grid: List<List<Boolean>>, color: Boolean): Set<Point<Boolean>> {
            val points = mutableSetOf<Point<Boolean>>()
            (0 until grid.size).forEach { row ->
                (0 until grid[row].size).forEach { column ->
                    if (grid[row][column] == color)
                        points.add(Point(column, row))
                }
            }
            return points
        }

        data class TestCase(
                val grid: List<List<Boolean>>,
                val start: Point<Boolean> ) {
            private val expectedUnflippedPoints = run {
                val graph = toGraph(grid, grid[start.row][start.column])
                val components = mutableSetOf<Set<Point<Boolean>>>()
                val exploredPoints = mutableSetOf<Point<Boolean>>()
                var currentComponent: MutableSet<Point<Boolean>>
                graph.keys.forEach {
                    point ->
                    if (exploredPoints.contains(point)) return@forEach
                    var points = setOf(point)
                    currentComponent = mutableSetOf()
                    while (points.isNotEmpty()) {
                        currentComponent.addAll(points)
                        points = nextLevel(graph, points, exploredPoints)
                    }
                    components.add(currentComponent)
                    exploredPoints.addAll(currentComponent)
                }
                components.filterNot { it.contains(start) }
                        .flatten().toSet()
            }
            operator fun component3() = expectedUnflippedPoints
        }

        val testCases = run {
            val testCaseCount = 10
            val random = RandomDataGenerator().apply { reSeed(1) }
            val testCases = mutableListOf<TestCase>()
            val gridDimensionRange = IntRange(1, 10)
            1.rangeTo(testCaseCount).forEach {
                val grid = mutableListOf<MutableList<Boolean>>()
                val rowCount = random.nextInt(gridDimensionRange)
                val columnCount = random.nextInt(gridDimensionRange)
                1.rangeTo(rowCount).forEach {
                    val row = mutableListOf<Boolean>()
                    grid.add(row)
                    1.rangeTo(columnCount).forEach {
                        row.add(random.nextInt(0, 1) == 1)
                    }
                }
                val start = Point<Boolean>(random.nextInt(0, columnCount - 1),
                        random.nextInt(0, rowCount - 1))
                testCases.add(TestCase(grid, start))
            }
            testCases
        }

    }
}