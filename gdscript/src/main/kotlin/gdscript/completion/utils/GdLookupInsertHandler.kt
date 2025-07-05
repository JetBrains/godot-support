package gdscript.competion.utils

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.hint.ShowParameterInfoHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import gdscript.utils.VirtualFileUtil.getPsiFile

class GdLookupInsertHandler : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val project = editor.project
        var valueToInsert = item.getObject().toString()
        val move = valueToInsert == "()_"
        if (move) {
            valueToInsert = "()"
        }
        if (project != null) {
            val model = editor.caretModel
            val matched = toAdd(editor, valueToInsert)
            if (matched > 0) {
                model.moveToOffset(model.offset + matched)
            }
            if (valueToInsert.length - matched > 0) {
                EditorModificationUtil.insertStringAtCaret(editor, valueToInsert.substring(matched))
                PsiDocumentManager.getInstance(project).commitDocument(editor.document)
                if (valueToInsert == "()" && move) {
                    model.moveToOffset(model.offset - 1)
                    if (CodeInsightSettings.getInstance().SHOW_PARAMETER_NAME_HINTS_ON_COMPLETION)
                        ShowParameterInfoHandler.invoke(
                                project,
                                editor,
                                editor.virtualFile.getPsiFile(project),
                                model.offset - 2,
                                null,
                                false,
                        )
                }
            }
        }
    }

    private fun toAdd(editor: Editor, valueToInsert: String): Int {
        val startOffset = editor.caretModel.offset
        val document = editor.document
        val valueLength = valueToInsert.length
        val maxLength = Math.min(document.textLength, startOffset + valueLength)
        val toCompare = document.getText(TextRange.create(startOffset, maxLength))
        var matched = 0
        for (ch in toCompare.toCharArray()) {
            if (ch == valueToInsert[matched]) {
                matched++
            } else {
                break
            }
        }
        return matched
    }

    companion object {
        val INSTANCE = GdLookupInsertHandler()
    }

}
