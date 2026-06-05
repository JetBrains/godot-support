package gdscript.competion.utils

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.util.TextRange

class GdLookupInsertHandler : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        if (editor.project == null) return
        val valueToInsert = item.getObject().toString()
        if (valueToInsert.isEmpty()) return

        val matched = toAdd(editor, valueToInsert)
        if (matched > 0) {
            editor.caretModel.moveToOffset(editor.caretModel.offset + matched)
        }
        if (valueToInsert.length - matched > 0) {
            EditorModificationUtil.insertStringAtCaret(editor, valueToInsert.substring(matched))
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
