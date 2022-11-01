package gdscript.completion.handler

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiDocumentManager

/**
 * Replaces inserted string with given input
 */
class GdReplaceInsertHandler(private val input: String) : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor;
        val project = context.project;

        val document = editor.document;
        document.replaceString(context.startOffset, context.selectionEndOffset, input);

        PsiDocumentManager.getInstance(project).commitDocument(document);
        editor.caretModel.moveToOffset(context.startOffset + input.length);
    }

}
