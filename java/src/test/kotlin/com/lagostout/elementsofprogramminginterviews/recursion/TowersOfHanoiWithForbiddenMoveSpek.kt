package com.lagostout.elementsofprogramminginterviews.recursion

import com.lagostout.elementsofprogramminginterviews.recursion.TowersOfHanoi.PegPosition
import com.lagostout.elementsofprogramminginterviews.recursion.TowersOfHanoi.PegPosition.*
import com.lagostout.elementsofprogramminginterviews.recursion.TowersOfHanoi.RingMove
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object TowersOfHanoiWithForbiddenMoveSpek : Spek({

    data class TestCase(val fromPegPosition: PegPosition,
                        val toPegPosition: PegPosition,
                        val ringCount: Int = 0,
                        val expectedOperationCount: Int? = null)

    val testCases = listOf(
            TestCase(LEFT, MIDDLE, expectedOperationCount = 0),
            TestCase(MIDDLE, RIGHT, expectedOperationCount = 0),
            TestCase(RIGHT, LEFT, expectedOperationCount = 0),
            TestCase(LEFT, RIGHT, expectedOperationCount = 0),
            TestCase(RIGHT, MIDDLE, expectedOperationCount = 0),
            TestCase(MIDDLE, LEFT, expectedOperationCount = 0),
            TestCase(LEFT, MIDDLE, 1, 2),
            TestCase(MIDDLE, RIGHT, 1, 1),
            TestCase(RIGHT, LEFT, 1, 1),
            TestCase(LEFT, RIGHT, 1, 1),
            TestCase(RIGHT, MIDDLE, 1, 1),
            TestCase(MIDDLE, LEFT, 1, 1),
            TestCase(LEFT, MIDDLE, 2, 7),
            TestCase(LEFT, MIDDLE, 3),
            TestCase(RIGHT, RIGHT, 5),
            TestCase(MIDDLE, RIGHT, 5),
            TestCase(RIGHT, MIDDLE, 5),
            TestCase(RIGHT, LEFT, 5),
            null).filterNotNull()

    testCases.forEach {
        (fromPegPosition, toPegPosition, ringCount, expectedOperationCount) ->
        given("ringCount: $ringCount, from peg: $fromPegPosition, " +
                "to peg: $toPegPosition") {
            it("moves rings between pegs ${ expectedOperationCount?.let {"in $it moves"} }") {
                val pegs = TowersOfHanoi.Pegs(ringCount, fromPegPosition)
                val operations = mutableListOf<RingMove>()
                minimumNUmberOfOperationsWithForbiddenMove(
                        pegs, pegs.at(fromPegPosition),
                        pegs.at(toPegPosition), operations)
                println(operations)
                println(operations.size)
                assertEquals(pegs, TowersOfHanoiSpek.pegsFromRunningOperations(
                        ringCount, fromPegPosition, operations))
                // Verify that operations performed are only the ones permitted.
                assertThat(operations.map { Pair(it.from, it.to) }, not(contains(
                        anyOf(equalTo(Pair(LEFT, MIDDLE)))
                )))
                // I'm only verifying expected operation count for trivial cases since
                // expected counts have to be computed by hand.
                expectedOperationCount?.let {
                    assertEquals(expectedOperationCount, operations.size)
                }
            }
        }
    }
})