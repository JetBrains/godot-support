package tscn.toolWindow.model

import tscn.psi.TscnNodeHeader
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

data class SceneNodeDrag(
    val nodeType: String,
    val nodeParentPath: String,
    val nodeName: String,
)

data class SceneDragPayload(
    val nodeMapping: HashMap<String, MutableList<TscnNodeHeader>>,
    val nodes: List<SceneNodeDrag>
)

class SceneNodeTransferable(
    private val payload: SceneDragPayload
) : Transferable {

    companion object {
        val SCENE_NODE_FLAVOR: DataFlavor =
            DataFlavor(
                DataFlavor.javaJVMLocalObjectMimeType + ";class=" + SceneDragPayload::class.java.name,
                "Godot Scene Node",
                SceneDragPayload::class.java.classLoader
            )

        fun isSceneTreeFlavor(transferFlavors: Array<DataFlavor>): Boolean {
            return transferFlavors.any { flavor ->
                (flavor.isMimeTypeEqual(SCENE_NODE_FLAVOR) &&
                    flavor.representationClass == SCENE_NODE_FLAVOR.representationClass)
            }
        }
    }

    override fun getTransferDataFlavors(): Array<DataFlavor> =
        arrayOf(SCENE_NODE_FLAVOR)

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean =
        isSceneTreeFlavor(arrayOf(flavor))

    override fun getTransferData(flavor: DataFlavor): Any {
        if (!isDataFlavorSupported(flavor)) throw UnsupportedFlavorException(flavor)
        return payload
    }
}
