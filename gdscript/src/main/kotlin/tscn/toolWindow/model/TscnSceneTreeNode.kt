package tscn.toolWindow.model

import tscn.psi.TscnNodeHeader
import javax.swing.tree.DefaultMutableTreeNode

class TscnSceneTreeNode : DefaultMutableTreeNode {

    var myName = ""
    var myType = ""
    var inherited = false
    var basePath = false

    constructor()
    constructor(name: String, type: String, nodePath: String) {
        myName = name
        myType = type
        userObject = nodePath
    }

    fun addNodeChild(node: TscnNodeHeader, externalType: String? = null) {
        val name = node.name
        val type = externalType ?: node.type
        val nodePath = node.nodePath
        if (name.isBlank()) return

        when (node.parentPath) {
            "" -> {
                myName = name
                myType = type
            }
            "." -> {
                insertInner(TscnSceneTreeNode(name, type, nodePath), 0.coerceAtLeast(node.index))
            }
            else -> {
                addNodeChild(name, type, nodePath, 0.coerceAtLeast(node.index), node.parentPath.split('/'))
            }
        }
    }

    fun addNodeChild(type: String, name: String, nodePath: String, index: Int, paths: List<String>) {
        if (paths.isEmpty()) {
            insertInner(TscnSceneTreeNode(name, type, nodePath), index)
        } else {
            val lookFor = paths.first()
            (children)
                .find { it is TscnSceneTreeNode && it.myName == lookFor }
                ?.let { (it as TscnSceneTreeNode).addNodeChild(type, name, nodePath, index, paths.subList(1, paths.size)) }
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
