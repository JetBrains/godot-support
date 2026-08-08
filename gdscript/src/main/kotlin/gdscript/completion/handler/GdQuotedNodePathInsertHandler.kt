package gdscript.completion.handler

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.lsp.stripDuplicateQuotesAdjacentToInserted

object GdQuotedNodePathInsertHandler : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        stripDuplicateQuotesAdjacentToInserted(context)
    }

}
