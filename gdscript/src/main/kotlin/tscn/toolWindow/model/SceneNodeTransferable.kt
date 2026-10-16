package tscn.toolWindow.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import tscn.psi.TscnNodeHeader
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.SystemFlavorMap
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.ByteArrayInputStream

@Serializable
data class SceneNodeDrag(
    val nodeType: String,
    val nodeParentPath: String,
    val nodeName: String,
    val isUnique: Boolean,
) {
    companion object {
        fun fromHeader(header: TscnNodeHeader): SceneNodeDrag {
            return SceneNodeDrag(
                header.type,
                header.parentPath,
                header.name,
                header.isUniqueNameOwner,
            )
        }
    }
}

@Serializable
data class SceneDragPayload(
    val nodeMapping: HashMap<String, MutableList<SceneNodeDrag>>,
    val nodes: List<SceneNodeDrag>
)

class SceneNodeTransferable(
    private val payload: SceneDragPayload
) : Transferable {

    companion object {
        private const val SCENE_NODE_MIME_TYPE = "application/x-jetbrains-godot-scene-nodes+json"

        val SCENE_NODE_FLAVOR: DataFlavor =
            DataFlavor(
                DataFlavor.javaJVMLocalObjectMimeType + ";class=" + SceneDragPayload::class.java.name,
                "Godot Scene Node",
                SceneDragPayload::class.java.classLoader
            )

        /** Provides the payload as UTF-8 JSON through a fresh input stream. */
        val NATIVE_SCENE_NODE_FLAVOR: DataFlavor =
            DataFlavor("$SCENE_NODE_MIME_TYPE;class=java.io.InputStream", SCENE_NODE_FLAVOR.humanPresentableName).apply {
                val flavorMap = SystemFlavorMap.getDefaultFlavorMap() as SystemFlavorMap
                flavorMap.addUnencodedNativeForFlavor(this, SCENE_NODE_MIME_TYPE)
                flavorMap.addFlavorForUnencodedNative(SCENE_NODE_MIME_TYPE, this)
            }

        fun isSceneTreeFlavor(flavor: DataFlavor): Boolean {
            return (flavor.isMimeTypeEqual(SCENE_NODE_FLAVOR) &&
                flavor.representationClass == SCENE_NODE_FLAVOR.representationClass)
        }
    }

    override fun getTransferDataFlavors(): Array<DataFlavor> =
        arrayOf(SCENE_NODE_FLAVOR, NATIVE_SCENE_NODE_FLAVOR)

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean =
        isSceneTreeFlavor(flavor) || flavor == NATIVE_SCENE_NODE_FLAVOR

    override fun getTransferData(flavor: DataFlavor): Any {
        return when {
            isSceneTreeFlavor(flavor) -> payload
            flavor == NATIVE_SCENE_NODE_FLAVOR -> ByteArrayInputStream(Json.encodeToString(payload).toByteArray(Charsets.UTF_8))
            else -> throw UnsupportedFlavorException(flavor)
        }
    }
}
