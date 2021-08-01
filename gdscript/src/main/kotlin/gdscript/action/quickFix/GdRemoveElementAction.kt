package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class GdRemoveElementAction : BaseIntentionAction {

    private val element: PsiElement;

    constructor(element: PsiElement) : super() {
        this.element = element;
    }

    override fun getText(): String {
        return "Remove";
    }

    override fun getFamilyName(): String {
        return "Remove";
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        this.element.delete();
    }

}
