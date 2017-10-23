package com.lagostout.elementsofprogramminginterviews.primitivetypes

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.Data3
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import kotlin.test.assertEquals

object SwapBitsSpek : Spek({
    describe("swapBits()") {
        val data = listOf<Data3<Long, Int, Int, Long>?>(
                data(0, 0, 0, 0),
                data(0, 1, 2, 0),
                data(1, 0, 1, 2),
                data(1, 0, 1, 2),
                // Negative numbers aren't a special case.
                // So no need to have more than one such case.
                data(-2, 0, 1, -3),
                data(10, 0, 1, 9),
                data(0b1010, 1, 2, 0b1100),
                null
        ).filterNotNull().toTypedArray()
        on("number %s, from %s, to %s", with = *data) {
            number, from, to, expected ->
            it("returns ${java.lang.Long.toBinaryString(expected)}") {
                assertEquals(expected, swapBits(number, from, to))
            }
        }
    }
})
