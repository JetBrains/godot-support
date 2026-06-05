package gdscript.competion.utils

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement

class GdMethodParenthesesInsertHandler(private val hasParameters: Boolean)
    : ParenthesesInsertHandler<LookupElement>() {

    override fun placeCaretInsideParentheses(context: InsertionContext, item: LookupElement): Boolean = hasParameters

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        super.handleInsert(context, item)
        if (hasParameters) {
            AutoPopupController.getInstance(context.project)
                .autoPopupParameterInfo(context.editor, null)
        }
    }
}
