package gdscript.action

import com.intellij.codeInsight.actions.SimpleCodeInsightAction
import com.intellij.codeInsight.folding.CodeFoldingManager
import com.intellij.codeInsight.folding.impl.EditorFoldingInfo
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.util.Consumer
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.action.util.GdCellRenderer
import gdscript.action.util.GdTraitClass
import gdscript.index.impl.GdFileResIndex
import gdscript.lineMarker.GdTraitLineMarkerContributor
import gdscript.psi.utils.GdFileUtil
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath

class GdAddTraitAction : SimpleCodeInsightAction() {

    override fun isValidForFile(project: Project, editor: Editor, file: PsiFile): Boolean {
        return file.fileType is GdFileType
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        if (!EditorModificationUtil.checkModificationAllowed(editor)) return

        val consumer = Consumer { it: GdTraitClass ->
            WriteCommandAction.writeCommandAction(project, file).run<Exception> {
                val traitFile = GdFileResIndex.getFiles(it.resource, project).firstOrNull() ?: return@run

                val titleLine = "${GdTraitLineMarkerContributor.PREFIX}${it.resource.substring("res://".length)}"
                val builder = StringBuilder()
                builder.appendLine(titleLine)
                builder.append(traitFile.getPsiFile(project)?.text?.replace("extends .+\n*".toRegex(), ""))
                builder.appendLine()
                builder.append(GdTraitLineMarkerContributor.SUFFIX)

                EditorModificationUtil.insertStringAtCaret(editor, builder.toString())
                PsiDocumentManager.getInstance(project).commitDocument(editor.document)

                editor.foldingModel.runBatchFoldingOperation {
                    val foldingManager = CodeFoldingManager.getInstance(project)
                    foldingManager.updateFoldRegions(editor)
                    editor.foldingModel.allFoldRegions.find {
                        val element = EditorFoldingInfo.get(editor).getPsiElement(it)
                        element is PsiComment && element.text == titleLine
                    }?.let {
                        it.isExpanded = false
                    }
                }
            }
        }

        JBPopupFactory.getInstance()
            .createPopupChooserBuilder(ArrayList(
                GdFileUtil.listTraits(project).map {
                    GdTraitClass(it.nameWithoutExtension, it.resourcePath())
                }
            ))
            .setRenderer(GdCellRenderer)
            .setTitle("Select Trait Class")
            .setItemChosenCallback(consumer)
            .setNamerForFiltering { o: GdTraitClass -> o.name }
            .createPopup()
            .showInBestPositionFor(editor)
    }

}
