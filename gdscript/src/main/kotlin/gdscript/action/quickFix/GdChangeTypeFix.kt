package gdscript.action.quickFix

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import gdscript.GdScriptBundle
import gdscript.psi.GdTypedVal

/**
 * Updates specified type
 */
class GdChangeTypeFix : BaseIntentionAction {

    private val element: GdTypedVal
    private val desired: String

    constructor(element: GdTypedVal, desired: String) {
        this.element = element
        this.desired = desired
    }

    override fun getText(): String {
        return GdScriptBundle.message("intention.name.change.type.to", desired)
    }

    override fun getFamilyName(): String {
        return GdScriptBundle.message("intention.name.change.type.to", desired)
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return element.isValid && PsiDocumentManager.getInstance(project).getDocument(element.containingFile) != null
    }

    // We open our own write command
    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (!element.isValid) return
        if (!FileModificationService.getInstance().preparePsiElementForWrite(element)) return

        val targetFile = element.containingFile
        val psiManager = PsiDocumentManager.getInstance(project)
        val targetDocument = psiManager.getDocument(targetFile) ?: return

        @Suppress("DialogTitleCapitalization")
        WriteCommandAction.writeCommandAction(project, targetFile).withName(text).run<Throwable> {
            psiManager.doPostponedOperationsAndUnblockDocument(targetDocument)
            targetDocument.replaceString(element.startOffset, element.endOffset, desired)
            psiManager.commitDocument(targetDocument)
            ReformatCodeProcessor(targetFile, false).run()
        }
    }
}
