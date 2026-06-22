package tscn.toolWindow.model

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorDropHandler
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.GdScriptBundle
import gdscript.utils.StringUtil.camelToSnakeCase
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.toolWindow.model.SceneNodeTransferable.Companion.SCENE_NODE_FLAVOR
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable

class SceneTreeFactoryListener : EditorFactoryListener {
    companion object {
        private val LOG = Logger.getInstance(SceneTreeFactoryListener::class.java)
    }

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor as? EditorImpl ?: return
        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return
        // TODO: C# file handling
        if (file.fileType !is GdFileType) return
        invokeLater {
            if (editor.isDisposed) {
                return@invokeLater
            }
            runCatching {
                editor.javaClass.getDeclaredMethod("getDropHandler").let {
                    it.isAccessible = true
                    val editorDropHandler = it.invoke(editor) as? EditorDropHandler ?: return@let
                    editor.setDropHandler(SceneTreeEditorDropHandler(editor, editorDropHandler))
                }
            }.onFailure { error -> LOG.error("Failed to create drop handler for scene tree: $error") }
        }
    }
}

private class SceneTreeEditorDropHandler(
    private val editor: Editor,
    private val delegate: EditorDropHandler
) : EditorDropHandler {

    companion object {
        private val LOG = Logger.getInstance(SceneTreeEditorDropHandler::class.java)
    }

    override fun canHandleDrop(transferFlavors: Array<DataFlavor>): Boolean {
        if (SceneNodeTransferable.isSceneTreeFlavor(transferFlavors)) {
            return true
        }
        return delegate.canHandleDrop(transferFlavors)
    }

    fun createOutput(payload: SceneDragPayload): String? {
        val (ctrlDown, altDown) = SceneNodeUtil.checkModifiers()

        data class ScriptPathAndName(val scriptParentPath: String, val scriptNodeName: String)

        val scriptInfo: ScriptPathAndName? by lazy {
            val targetFile = FileDocumentManager.getInstance().getFile(editor.document) ?: return@lazy null
            val scriptResource = targetFile.resourcePath()
            val nodes = payload.nodeMapping[scriptResource] ?: return@lazy null
            val scriptParentPath: String
            val scriptNodeName: String
            if (nodes.isEmpty()) {
                LOG.error("no node was found for script ${scriptResource}, cannot insert node location properly")
                return@lazy null
            }
            // TODO: potential improvement to show a yellow squiggle with a warning that
            // the resolution will fail for all but the first node if more than one node has
            // the script -> nodes.size > 1
            val first = nodes.first()
            scriptParentPath = first.parentPath
            scriptNodeName = first.name
            return@lazy ScriptPathAndName(scriptParentPath, scriptNodeName)
        }

        fun relativePath(nodeParentPath: String, nodeName: String): String? {
            return SceneNodePathResolver.constructRelativePath(
                scriptInfo?.scriptParentPath ?: return null,
                nodeParentPath,
                nodeName,
                scriptInfo?.scriptNodeName ?: return null
            )
        }

        // TODO: C# file handling
        fun assembleFinalText(nodeParent: String, nodeName: String, nodeType: String): String? {
            // Godot node names can start with a number, for some reason
            val name_prefix = if (nodeName.firstOrNull()?.isDigit() ?: false) {
               "_"
            } else {
                ""
            }
            return when {
                ctrlDown -> "@onready var $name_prefix${nodeName.camelToSnakeCase()}: ${nodeType} = ${
                    relativePath(
                        nodeParent,
                        nodeName
                    ) ?: return null
                }"

                altDown -> "@export var $name_prefix${nodeName.camelToSnakeCase()}: ${nodeType}"
                else -> relativePath(nodeParent, nodeName) ?: return null
            }
        }

        val output = StringBuilder()
        for (item in payload.nodes) {
            val nextItem = assembleFinalText(item.nodeParentPath, item.nodeName, item.nodeType)
            if (nextItem == null) {
                continue
            }
            output.append(nextItem + "\n")
        }
        val outputText = output.toString()
        if (outputText.isEmpty()) {
            return null
        }
        return outputText
    }


    override fun handleDrop(t: Transferable, project: Project?, editorWindow: EditorWindow?) {
        if (project == null) return

        if (!SceneNodeTransferable.isSceneTreeFlavor(t.transferDataFlavors)) {
            delegate.handleDrop(t, project, editorWindow)
            return
        }

        val payload = t.getTransferData(SCENE_NODE_FLAVOR) as? SceneDragPayload ?: return

        val textToInsert = createOutput(payload) ?: return

        WriteCommandAction.writeCommandAction(project)
            .withName(GdScriptBundle.message("gdscript.scene.tree.insert.action.name"))
            .run<RuntimeException> {
                val offset = editor.caretModel.offset
                editor.document.insertString(offset, textToInsert)
            }

    }
}
