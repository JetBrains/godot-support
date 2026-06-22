package tscn.toolWindow.model

import com.intellij.openapi.application.EDT
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorDropHandler
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.GdScriptBundle
import gdscript.utils.StringUtil.camelToSnakeCase
import gdscript.utils.VirtualFileUtil.resourcePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tscn.toolWindow.model.SceneNodeTransferable.Companion.SCENE_NODE_FLAVOR
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable

class SceneTreeFactoryListener : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor as? EditorImpl ?: return
        val project = editor.project ?: return
        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return
        if (file.fileType !is GdFileType && !file.extension.equals("cs", true)) return
        if (GodotCommunityUtil.isGodotProject(project)) {
            SceneTreeEditorDropHandler.installIntoEditor(editor, project)
        }
    }
}

class SceneTreeEditorDropHandler(
    private val editor: Editor,
    private val delegate: EditorDropHandler
) : EditorDropHandler {

    companion object {
        private val LOG = Logger.getInstance(SceneTreeEditorDropHandler::class.java)
        private val SCENE_DROP_HANDLER = Key.create<Boolean>("gdscript.sceneTreeDropHandlerInstalled")
        fun installIntoEditor(editor: EditorImpl, project: Project) {
            GdScriptProjectLifetimeService.getScope(project).launch {
                withContext(Dispatchers.EDT) {
                    if (editor.isDisposed) return@withContext
                    if (editor.getUserData(SCENE_DROP_HANDLER) == true) return@withContext
                    runCatching {
                        editor.javaClass.getDeclaredMethod("getDropHandler").let {
                            it.isAccessible = true
                            val editorDropHandler = it.invoke(editor) as? EditorDropHandler ?: return@let
                            editor.setDropHandler(SceneTreeEditorDropHandler(editor, editorDropHandler))
                            editor.putUserData(SCENE_DROP_HANDLER, true)
                        }
                    }.onFailure { error -> LOG.warn("Failed to create drop handler for scene tree: ", error) }
                }
            }
        }

        fun installIntoExistingEditors(project: Project) {
            val editorFactory = EditorFactory.getInstance()
            editorFactory.addEditorFactoryListener(
                SceneTreeFactoryListener(),
                GdScriptProjectLifetimeService.getInstance(project)
            )
            editorFactory.allEditors.asSequence().filter {
                it.project == project
            }.forEach {
                val editor = it as? EditorImpl ?: return@forEach
                val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return@forEach
                if (file.fileType !is GdFileType && !file.extension.equals("cs", true)) return@forEach
                installIntoEditor(editor, project)
            }
        }
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

        val targetFile = FileDocumentManager.getInstance().getFile(editor.document) ?: return null

        val scriptInfo: ScriptPathAndName? by lazy {
            val scriptResource = targetFile.resourcePath()
            val nodes = payload.nodeMapping[scriptResource] ?: return@lazy null
            val scriptParentPath: String
            val scriptNodeName: String
            if (nodes.isEmpty()) {
                LOG.warn("no node was found for script ${scriptResource}, cannot insert node location properly")
                return@lazy null
            }
            // TODO: potential improvement to show a yellow squiggle with a warning that
            // the resolution will fail for all but the first node if more than one node has
            // the script -> nodes.size > 1
            val first = nodes.first()
            scriptParentPath = first.nodeParentPath
            scriptNodeName = first.nodeName
            return@lazy ScriptPathAndName(scriptParentPath, scriptNodeName)
        }
        val isCsFile = targetFile.extension.equals("cs", true)
        fun relativePath(nodeParentPath: String, nodeName: String, isUnique: Boolean): String? {
            return SceneNodePathResolver.constructRelativePath(
                scriptInfo?.scriptParentPath ?: return null,
                nodeParentPath,
                nodeName,
                scriptInfo?.scriptNodeName ?: return null,
                isUnique = isUnique,
                language = if (isCsFile) {
                    SceneNodePathResolver.TargetLanguage.CSharp
                } else {
                    SceneNodePathResolver.TargetLanguage.GdScript
                },
            )
        }

        // TODO: C# file handling -> C# requires more than just simple inplace codegen.
        fun assembleFinalText(nodeParent: String, nodeName: String, nodeType: String, isUnique: Boolean): String? {
            // Godot node names can start with a number, for some reason
            val namePrefix = if (nodeName.firstOrNull()?.isDigit() ?: false) {
                "_"
            } else {
                ""
            }
            return when {
                isCsFile -> relativePath(nodeParent, nodeName, isUnique)
                ctrlDown -> "@onready var $namePrefix${nodeName.camelToSnakeCase()}: $nodeType = ${
                    relativePath(
                        nodeParent,
                        nodeName,
                        isUnique
                    ) ?: return null
                }"

                altDown -> "@export var $namePrefix${nodeName.camelToSnakeCase()}: $nodeType"
                else -> relativePath(nodeParent, nodeName, isUnique) ?: return null
            }
        }

        val output = StringBuilder()
        for (item in payload.nodes) {
            val nextItem = assembleFinalText(item.nodeParentPath, item.nodeName, item.nodeType, item.isUnique)
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
