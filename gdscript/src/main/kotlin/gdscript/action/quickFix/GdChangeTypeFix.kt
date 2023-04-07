package gdscript.action.quickFix

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import gdscript.psi.GdTypedVal

/**
 * Updates specified type
 */
class GdChangeTypeFix : BaseIntentionAction {

    private val element: GdTypedVal
    private val desired: String;

    constructor(element: GdTypedVal, desired: String) {
        this.element = element
        this.desired = desired
    }

    override fun getText(): String {
        return "Change type to $desired"
    }

    override fun getFamilyName(): String {
        return "Change type to $desired"
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) return

        val psiManager = PsiDocumentManager.getInstance(project);
        psiManager.doPostponedOperationsAndUnblockDocument(editor.document)

        editor.document.replaceString(element.startOffset, element.endOffset, desired)
        psiManager.commitDocument(editor.document)
        ReformatCodeProcessor(file, false).run()
    }
}
