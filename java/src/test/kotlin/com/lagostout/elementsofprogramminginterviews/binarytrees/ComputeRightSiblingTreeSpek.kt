package com.lagostout.elementsofprogramminginterviews.binarytrees

import com.lagostout.common.takeFrom
import com.lagostout.datastructures.BinaryTreeNode
import com.lagostout.datastructures.BinaryTreeNode.Companion.bbtr
import com.lagostout.datastructures.BinaryTreeNode.Companion.levels
import com.lagostout.datastructures.RawBinaryTreeNode.Companion.rbt
import com.lagostout.elementsofprogramminginterviews.binarytrees.ComputeRightSiblingTree.computeRightSiblingTree
import com.lagostout.kotlin.common.Alphabet.*
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

object ComputeRightSiblingTreeSpek : Spek({

    fun <Alphabet> computeExpected(root: BinaryTreeNode<Alphabet>):
            List<BinaryTreeNode<Alphabet>?> {
        return levels(root).flatMap {
//            println("level $it")
            it.takeFrom(1).toMutableList<BinaryTreeNode<Alphabet>?>() + listOf(null)
        }
    }

    val data = listOfNotNull(
        bbtr(listOf(rbt(A))),
        bbtr(listOf(rbt(A, left = 1, right = 2), rbt(B), rbt(C))),
        null
    ).map {
        data(it, computeExpected(it))
    }.toTypedArray()

    describe("computeRightSiblingTree") {
        on("root %s", with = *data) { root, expected ->
            computeRightSiblingTree(root)
            it("should set levelNext values to $expected") {
                val levelNexts = levels(root).flatMap {
                    it.map {
                        it.levelNext
                    }
                }
                assertThat(levelNexts).containsExactlyElementsOf(expected)
            }
        }
    }

})