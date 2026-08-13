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
import gdscript.psi.utils.GdNodeUtil
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
    override fun canHandleDrop(transferFlavors: Array<DataFlavor>): Boolean {
        if (SceneNodeTransferable.isSceneTreeFlavor(transferFlavors)) {
            return true
        }
        return delegate.canHandleDrop(transferFlavors)
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

    data class ScriptPathAndName(val scriptParentPath: String, val scriptNodeName: String)

    data class SceneTreeEditorDropDependencies(
        val nodeParent: String,
        val nodeName: String,
        val nodeType: String,
        val isUnique: Boolean,
        val isCsFile: Boolean,
        val ctrlDown: Boolean,
        val altDown: Boolean,
        val scriptInfo: ScriptPathAndName?
    )

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

        // TODO: C# file handling -> C# requires more than just simple inplace codegen.
        fun assembleFinalText(
            deps: SceneTreeEditorDropDependencies
        ): String? {
            // Godot node names may start with a digit or hold punctuation, an identifier may not
            val varName = GdNodeUtil.nodeNameToIdentifier(deps.nodeName)
            val relativePath = relativePath(
                deps.nodeParent,
                deps.nodeName,
                deps.isUnique,
                deps.scriptInfo,
                deps.isCsFile,
            ) ?: return null
            return when {
                deps.isCsFile -> "GetNode<${deps.nodeType}>($relativePath);"
                deps.ctrlDown -> "@onready var $varName: ${deps.nodeType} = $relativePath"
                deps.altDown -> "@export var $varName: ${deps.nodeType}"
                else -> relativePath
            }
        }

        private fun relativePath(
            nodeParentPath: String,
            nodeName: String,
            isUnique: Boolean,
            scriptInfo: ScriptPathAndName?,
            isCsFile: Boolean
        ): String? {
            return SceneNodePathResolver.constructRelativePath(
                scriptInfo?.scriptParentPath ?: return null,
                nodeParentPath,
                nodeName,
                scriptInfo.scriptNodeName,
                isUnique = isUnique,
                language = if (isCsFile) {
                    SceneNodePathResolver.TargetLanguage.CSharp
                } else {
                    SceneNodePathResolver.TargetLanguage.GdScript
                },
            )
        }
    }

    fun createOutput(payload: SceneDragPayload): String? {
        val (ctrlDown, altDown) = SceneNodeUtil.checkModifiers()

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

        val output = StringBuilder()
        for ((nodeType, nodeParentPath, nodeName, isUnique) in payload.nodes) {
            val deps = SceneTreeEditorDropDependencies(
                nodeParent = nodeParentPath,
                nodeName = nodeName,
                nodeType = nodeType,
                isUnique = isUnique,
                isCsFile = isCsFile,
                ctrlDown = ctrlDown,
                altDown = altDown,
                scriptInfo = scriptInfo
            )
            val nextItem = assembleFinalText(deps)
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
}
