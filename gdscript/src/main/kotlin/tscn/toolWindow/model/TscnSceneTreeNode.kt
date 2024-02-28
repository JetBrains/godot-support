package tscn.toolWindow.model

import tscn.psi.TscnNodeHeader
import javax.swing.tree.DefaultMutableTreeNode

class TscnSceneTreeNode : DefaultMutableTreeNode {

    var myName = ""
    var myType = ""
    var inherited = false

    constructor()
    constructor(name: String, type: String) {
        myName = name
        myType = type
    }

    fun addNodeChild(node: TscnNodeHeader, externalType: String? = null) {
        val name = node.name
        val type = externalType ?: node.type
        if (name.isBlank()) return

        when (node.parentPath) {
            "" -> {
                userObject = node
                myName = name
                myType = type
            }
            "." -> {
                insertInner(TscnSceneTreeNode(name, type), 0.coerceAtLeast(node.index))
            }
            else -> {
                addNodeChild(name, type, 0.coerceAtLeast(node.index), node.parentPath.split('/'))
            }
        }
    }

    fun addNodeChild(type: String, name: String, index: Int, paths: List<String>) {
        if (paths.isEmpty()) {
            insertInner(TscnSceneTreeNode(name, type), index)
        } else {
            val lookFor = paths.first()
            (children)
                .find { it is TscnSceneTreeNode && it.myName == lookFor }
                ?.let { (it as TscnSceneTreeNode).addNodeChild(type, name, index, paths.subList(1, paths.size)) }
        }
    }

    private fun insertInner(node: TscnSceneTreeNode, index: Int) {
        if (childCount > index) {
            val i = children.indexOfFirst { it is TscnSceneTreeNodePlaceholder }
            if (i >= 0) {
                node.inherited = true
                children[i] = node
            }
            return
        }

        if (childCount < index) {
            add(TscnSceneTreeNodePlaceholder())
        }
        insert(node, index)
    }

}
