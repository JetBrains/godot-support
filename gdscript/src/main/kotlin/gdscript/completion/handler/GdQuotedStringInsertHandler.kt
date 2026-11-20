package gdscript.completion.handler

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement

// Handles caret movement after the closing quote for code completions.
object GdQuotedStringInsertHandler : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val tail = context.tailOffset
        val caret = context.editor.caretModel
        val next = context.document.charsSequence.getOrNull(tail)

        if (caret.offset == tail && (next == '"' || next == '\'')) {
            caret.moveToOffset(tail + 1)
        }
    }

}
