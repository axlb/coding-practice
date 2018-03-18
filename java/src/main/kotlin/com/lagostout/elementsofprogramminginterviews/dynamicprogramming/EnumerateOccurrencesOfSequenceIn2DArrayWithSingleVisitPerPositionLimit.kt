package com.lagostout.elementsofprogramminginterviews.dynamicprogramming

import com.lagostout.common.down
import com.lagostout.common.left
import com.lagostout.common.right
import com.lagostout.common.up

/* Problem 17.5.3 page 322 */

object EnumerateOccurrencesOfSequenceIn2DArrayWithSingleVisitPerPositionLimit {

    fun computeWithRecursionAndBruteForce(
            grid: List<List<Int>>, pattern: List<Int>):
            Set<List<Pair<Int, Int>>> {
        return when {
            grid.isEmpty() || pattern.isEmpty() -> emptySet()
            else -> {
                val rowCount = grid.lastIndex
                val colCount = grid.first().lastIndex
                val positions = grid.withIndex().flatMap {
                        (rowIndex, row) ->
                    row.indices.map {
                        Pair(rowIndex, it)
                    }
                }
                fun compute(start: Pair<Int, Int>, patternIndex: Int,
                            visited: Set<Pair<Int, Int>> = setOf(),
                            path: List<Pair<Int, Int>> = listOf()):
                        Set<List<Pair<Int, Int>>> {
                    return when {
                        grid[start.first][start.second] !=
                                pattern[patternIndex] -> emptySet()
                        patternIndex == pattern.lastIndex ->
                            setOf(path + listOf(start))
                        else -> start.run {
                            listOf(up, down, left, right)
                        }.filter {
                            it !in visited && it.first in (0..rowCount) &&
                                    it.second in (0..colCount)
                        }.map {
                            compute(it, patternIndex + 1, visited + setOf(start),
                                path + listOf(start))
                        }.fold(mutableSetOf()) { acc, curr ->
                            acc.apply { addAll(curr) }
                        }
                    }
                }
                fun search(positionsIndex: Int): Set<List<Pair<Int, Int>>> {
                    return if (positionsIndex > positions.lastIndex) emptySet()
                    else search(positionsIndex + 1).toMutableSet().apply {
                        addAll(compute(positions[positionsIndex], 0))
                    }
                }
                return search(0)
            }
        }
    }

    fun computeWithRecursionAndMemoization(
            grid: List<List<Int>>, pattern: List<Int>): Boolean {
        return when {
            grid.isEmpty() -> pattern.isEmpty()
            pattern.isEmpty() -> true
            else -> {
                val lastRow = grid.lastIndex
                val lastCol = grid.first().lastIndex
                val positions = grid.withIndex().flatMap { (rowIndex, row) ->
                    row.indices.map {
                        Pair(rowIndex, it)
                    }
                }
                val cache = mutableMapOf<Pair<Int, Int>, MutableList<Boolean?>>()
                fun compute(
                        start: Pair<Int, Int>, patternIndex: Int,
                        visited: MutableSet<Pair<Int, Int>> = mutableSetOf()): Boolean {
                    val isValidPosition = start.first in (0..lastRow) &&
                                start.second in (0..lastCol)
                    return cache[start]?.get(patternIndex) ?: when {
                        !isValidPosition -> false
                        grid[start.first][start.second] !=
                                pattern[patternIndex] -> false
                        patternIndex == pattern.lastIndex -> true
                        else -> start.run {
                            listOf(up, down, left, right).filter {
                                it !in visited
                            }
                        }.map {
                            compute(it, patternIndex + 1, visited.apply { add(start) })
                        }.any { it }
                    }.also {
                        cache.getOrPut(start){
                            MutableList(pattern.size) { null }
                        }[patternIndex] = it
                    }
                }
                fun search(positionsIndex: Int): Boolean {
                    return if (positionsIndex > positions.lastIndex) false
                    // Swapping the OR operands short-circuits the search,
                    // completing as soon as the first sequence is found.
                    // This prevents the cache from being useful.  Having
                    // them as follows makes use of the cache, as every
                    // possible sequence in the grid is explored.
                    else search(positionsIndex + 1) ||
                            compute(positions[positionsIndex], 0)

                }
                return search(0)
            }
        }
    }

    fun computeBottomUpWithMemoization(
            grid: List<List<Int>>, pattern: List<Int>): Boolean {
        return when {
            grid.isEmpty() -> pattern.isEmpty()
            pattern.isEmpty() -> true
            else -> {
                val lastCacheRow = grid.lastIndex
                val lastCacheCol = grid.first().lastIndex
                val positions = (0..lastCacheRow).flatMap { row ->
                    (0..lastCacheCol).map { col ->
                        Pair(row, col)
                    }
                }
                val cache = positions.map { position ->
                    position to (-1..pattern.lastIndex).map { patternIndex ->
                        patternIndex to Pair(
                            if (patternIndex == -1) true else null,
                            setOf<Set<Pair<Int, Int>>>())
                    }.toMap().toMutableMap()
                }.toMap()
                (-1 until pattern.lastIndex).forEach { patternIndex ->
                    positions.filter {
                        cache[it]?.let {
                            it[patternIndex]?.first
                        } == true
                    }.forEach { position ->
                        position.run {
                            (if (patternIndex == -1) listOf(
                                Pair(position, setOf(emptySet())))
                            else (listOf(up, down, left, right).map { nextPosition ->
                                val paths = cache[position]?.get(patternIndex)?.second!!
                                Pair(nextPosition, paths.filter { nextPosition !in it }.toSet())
                            }.filter {
                                it.second.isNotEmpty()
                            })).map {
                                Pair(it.first, it.second.map {
                                    it.toMutableSet().apply { add(position) }.toSet()
                                }.toMutableSet())
                            }.toSet()
                        }.forEach { (nextPosition, newPaths) ->
                            val nextPatternIndex = patternIndex + 1
                            cache[nextPosition]?.let { patternIndexToPathsMap ->
                                patternIndexToPathsMap.merge(
                                    nextPatternIndex,
                                    Pair(pattern[nextPatternIndex] ==
                                            grid[nextPosition.first][nextPosition.second],
                                        setOf())) { old, new ->
                                    if (new.first == true) {
                                        val oldPaths = patternIndexToPathsMap[nextPatternIndex]!!.second
                                        Pair(new.first, new.second.toMutableSet().apply {
                                            addAll(oldPaths)
                                            addAll(newPaths)
                                        })
                                    } else old
                                }
                            }
                        }
                    }
                }
                cache.any { it.value[pattern.lastIndex]?.first == true }
            }
        }
    }

}