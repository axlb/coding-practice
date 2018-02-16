package com.lagostout.bytebybyte.dynamicprogramming

import com.lagostout.common.nextBoolean
import com.lagostout.common.nextInt
import org.apache.commons.math3.random.RandomDataGenerator
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xdescribe
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.paukov.combinatorics3.Generator
import kotlin.math.absoluteValue

object TargetSumSpek : Spek({

    val data by memoized {
        val random = RandomDataGenerator().apply { reSeed(1) }
        val caseCount = 1000
        val numberRange = (1..10)
        (1..caseCount).map {
            val numberCount = random.nextInt(0,5)
            val case = mutableSetOf<Int>()
            while (case.size < numberCount) {
                random.nextInt(numberRange).let {
                    case.add(it)
                }
            }
            case.flatMap {
                listOf(it, -it) }.let {
                Generator.combination(it).simple(numberCount) }.filter {
                it.map { it.absoluteValue }.toSet().size == numberCount }.groupBy {
                it.sum() }.let { map ->
                if (random.nextBoolean()) map.keys.toList().let { // A reachable target.
                    if (it.isEmpty()) Pair(0, 1) // There's one way to reach 0 with 0 numbers.
                    else {
                        val keyIndex = random.nextInt(0 until it.size)
                        val key = it[keyIndex]
                        Pair(key, map[key]!!.count())
                    }
                } else { // An unreachable target.
                    var target: Int
                    val unreachableSum = case.sum() + 1
                    do {
                        target = random.nextInt(-unreachableSum..unreachableSum)
                    } while (map.keys.contains(target))
                    Pair(target, 0)
                }
            }.let { (target, expected) ->
                        data(case.toList(), target, expected) }
        }.toTypedArray()
    }

    xdescribe("computeWithBruteForceAndRecursion") {
        on("numbers: %s, target: %s", with = *data) { numbers, target, expected ->
            it("should return $expected") {
                assertThat(TargetSum.computeWithBruteForceAndRecursion(numbers, target))
                        .isEqualTo(expected)
            }
        }
    }

    describe("computeWithRecursionAndMemoization") {
        on("numbers: %s, target: %s", with = *data) { numbers, target, expected ->
            it("should return $expected") {
                assertThat(TargetSum.computeWithRecursionAndMemoization(numbers, target))
                        .isEqualTo(expected)
            }
        }
    }

})

