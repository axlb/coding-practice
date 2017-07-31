package com.lagostout.elementsofprogramminginterviews.binarysearchtrees

import com.lagostout.common.BinaryTreeNode
import com.lagostout.datastructures.RawBinaryTreeNode
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

class ComputeLCAInBSTSpek : Spek({
    describe("computeLCAInBST") {
        testCases.forEach {
            (root, firstDescendant, secondDescendant, expectedLCA) ->
            given("root $root, firstDescendant $firstDescendant, " +
                    "secondDescendant $secondDescendant") {
                it("finds the LCA as $expectedLCA") {
                    assertEquals(expectedLCA, computeLCAInBST(
                            root, firstDescendant, secondDescendant))
                }
            }
        }
    }
}) {
    companion object {

        class TestCase(rawNodes: List<RawBinaryTreeNode<Int>> = emptyList(),
                       val firstDescendant: Int = -1,
                       val secondDescendant: Int = -1,
                       val expectedLCAIndex: Int? = null) {
            val root: BinaryTreeNode<Int>?
            val expectedLCA: BinaryTreeNode<Int>?

            init {
                val result = BinaryTreeNode.buildBinaryTree(rawNodes)
                root = result.left
                expectedLCA = expectedLCAIndex?.run { result.right[this] }
            }

            operator fun component1() = root
            operator fun component2() = firstDescendant
            operator fun component3() = secondDescendant
            operator fun component4() = expectedLCA
        }

        val testCases: List<TestCase> = run {
            listOf(
                    TestCase(),
                    /**
                     *  1
                     */
                    TestCase(listOf(RawBinaryTreeNode(value = 1))),
                    TestCase(listOf(RawBinaryTreeNode(value = 1)), firstDescendant = 1),
                    TestCase(listOf(RawBinaryTreeNode(value = 1)), secondDescendant = 1),
                    TestCase(listOf(RawBinaryTreeNode(value = 1)),
                            firstDescendant = 1, secondDescendant = 1, expectedLCAIndex = 0),
                    /**
                     *    1
                     *   /
                     *  0
                     */
                    TestCase(listOf(RawBinaryTreeNode(leftChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 0)), firstDescendant = 0,
                            secondDescendant = 1, expectedLCAIndex = 0),
                    // First/last descendants reversed
                    TestCase(listOf(RawBinaryTreeNode(leftChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 0)), firstDescendant = 1,
                            secondDescendant = 0, expectedLCAIndex = 0),
                    // Descendant missing
                    TestCase(listOf(RawBinaryTreeNode(leftChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 0)), firstDescendant = 2,
                            secondDescendant = 0),
                    TestCase(listOf(RawBinaryTreeNode(leftChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 0)), firstDescendant = 1,
                            secondDescendant = -1),
                    // Descendants missing
                    TestCase(listOf(RawBinaryTreeNode(leftChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 0)), firstDescendant = 2,
                            secondDescendant = -1),
                    /**
                     *  1
                     *   \
                     *    2
                     */
                    TestCase(listOf(RawBinaryTreeNode(rightChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 2)), firstDescendant = 0,
                            secondDescendant = 1, expectedLCAIndex = 0),
                    // First/last descendants reversed
                    TestCase(listOf(RawBinaryTreeNode(rightChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 2)), firstDescendant = 1,
                            secondDescendant = 0),
                    // Descendant missing
                    TestCase(listOf(RawBinaryTreeNode(rightChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 2)), firstDescendant = 1,
                            secondDescendant = 3),
                    // Descendants missing
                    TestCase(listOf(RawBinaryTreeNode(rightChildIndex = 1, value = 1),
                            RawBinaryTreeNode(value = 2)), firstDescendant = 0,
                            secondDescendant = 3),
                    // TODO More cases
                    null).filterNotNull()
        }
    }
}