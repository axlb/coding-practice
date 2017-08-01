package com.lagostout.elementsofprogramminginterviews.binarytrees

import com.lagostout.common.*
import com.lagostout.elementsofprogramminginterviews.binarytrees.Step.*

enum class Step {
    PROCESS_NODE, LEFT_DESCENDANT,
    RIGHT_DESCENDANT, RIGHT_ANCESTOR, BREAK
}

fun <T : Comparable<T>> inOrderTraversalWithConstantSpace(
        root: BinaryTreeNode<T>): List<T> {
    var currentNode = root
    var step: Step = LEFT_DESCENDANT
    val traversalOrder = mutableListOf<T>()
    whileAllNodesHaveNotBeenExplored@ while (true) {
        when (step) {
            LEFT_DESCENDANT -> {
                step = currentNode.left?.run {
                    currentNode = this
                    LEFT_DESCENDANT
                } ?: PROCESS_NODE

            }
            PROCESS_NODE -> {
                traversalOrder.add(currentNode.value)
                step = RIGHT_DESCENDANT
            }
            RIGHT_DESCENDANT -> {
                step = currentNode.right?.run {
                    currentNode = this
                    LEFT_DESCENDANT
                } ?: RIGHT_ANCESTOR
            }
            RIGHT_ANCESTOR -> {
                step = currentNode.rightAncestor?.run {
                    currentNode = this
                    PROCESS_NODE
                } ?: run {
                    if (currentNode.isRoot) {
                        BREAK
                    } else {
                        currentNode = currentNode.parent
                        RIGHT_ANCESTOR
                    }
                }
            }
            BREAK -> {
                break@whileAllNodesHaveNotBeenExplored
            }
        }
    }
    return traversalOrder
}
