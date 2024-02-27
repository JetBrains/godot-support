package tscn.toolWindow.model

import tscn.psi.TscnNodeHeader
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class TscnSceneTreeNode : DefaultMutableTreeNode {

    var myName = ""
    var myType = ""

    constructor()
    constructor(name: String, type: String) {
        myName = name
        myType = type
    }

    fun addNodeChild(node: TscnNodeHeader) {
        val name = node.name
        val type = node.type
        if (name.isBlank() || type.isBlank()) return

        when (node.parentPath) {
            "" -> {
                userObject = node
                myName = name
                myType = type
            }
            "." -> {
                // TODO
                // root.children[node.index] = TscnSceneTreeElement(node.type, node.name)
                add(TscnSceneTreeNode(name, type))
            }
            else -> {
                addNodeChild(name, type, node.index, node.parentPath.split('/'))
            }
        }
    }

    fun addNodeChild(type: String, name: String, index: Int, paths: List<String>) {
        if (paths.isEmpty()) {
            add(TscnSceneTreeNode(name, type))
        } else {
            val lookFor = paths.first()
            (children as Vector<TscnSceneTreeNode>)
                .find { it.myName == lookFor }
                ?.addNodeChild(type, name, index, paths.subList(1, paths.size))
        }
    }

}
