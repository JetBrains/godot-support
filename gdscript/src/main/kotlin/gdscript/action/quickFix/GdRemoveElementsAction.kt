package gdscript.action.quickFix

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class GdRemoveElementsAction : BaseIntentionAction {

    private val elements: Array<out PsiElement>

    constructor(vararg elements: PsiElement) : super() {
        this.elements = elements
    }

    override fun getText(): String {
        return GdScriptBundle.message("intention.name.remove.element")
    }

    override fun getFamilyName(): String {
        return GdScriptBundle.message("intention.family.name.remove.element")
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        this.elements.forEach { it.delete() }
        if (file != null) ReformatCodeProcessor(file, false).run()
    }
}
