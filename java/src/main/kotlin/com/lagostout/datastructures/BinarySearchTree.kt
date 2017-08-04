package com.lagostout.datastructures

import com.google.common.annotations.VisibleForTesting

interface BinarySearchTreeable <T : Comparable<T>> {
    fun find(key: T): Pair<BinaryTreeNode<T>?, BinaryTreeNode<T>?>?
    fun next(key: T): BinaryTreeNode<T>
    fun insert(key: T)
    fun delete(key: T)
    fun nearestNeighbors(key: T): List<BinaryTreeNode<T>>
    fun rangeSearch(startKey: T, endKey: T): List<BinaryTreeNode<T>>
    fun contains(key: T): Boolean
    @VisibleForTesting
    var root: BinaryTreeNode<T>?
}

class BinarySearchTree<T : Comparable<T>> : BinarySearchTreeable<T> {

    override var root: BinaryTreeNode<T>? = null

    class Iterator<T : Comparable<T>>  : kotlin.collections.Iterator<BinaryTreeNode<T>> {

        override fun hasNext(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun next(): BinaryTreeNode<T> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    /**
     * Returns pair of nodes.  If a node with value <code>key</code>, is found
     * it'll be the first in the pair.  Otherwise, the second in the pair will
     * be the node that would be the sought node's parent, if the sought node
     * were in the tree.
     */
    override fun find(key: T): Pair<BinaryTreeNode<T>?, BinaryTreeNode<T>?>? {
        var result: Pair<BinaryTreeNode<T>?, BinaryTreeNode<T>?>? = null
        root?.apply {
            var currentNode: BinaryTreeNode<T> = this
            while (true) {
                if (value == key)
                    break
                else if (value > key) {
                    currentNode.right?.apply {
                        currentNode = this
                    }?: break
                } else {
                    currentNode.left?.apply {
                        currentNode = this
                    }?: break
                }
            }
            result = currentNode.let {
                if (value == key) Pair(this, null)
                else Pair(null, this)
            }
        }
        return result
    }

    override fun insert(key: T) {
        val newNode = BinaryTreeNode(value = key)
        find(key)?.also {
            (soughtNode, insertionPoint) ->
            soughtNode?.apply {
                return@insert
            }
            insertionPoint?.apply {
                if (value != key) {
                    if (value > key)
                        insertionPoint.left = newNode
                    else
                        insertionPoint.right = newNode
                    newNode.parent = insertionPoint
                }
                return@insert
            }
            root = newNode
        }
    }

    override fun contains(key: T): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun next(key: T): BinaryTreeNode<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(key: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nearestNeighbors(key: T): List<BinaryTreeNode<T>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rangeSearch(startKey: T, endKey: T): List<BinaryTreeNode<T>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}