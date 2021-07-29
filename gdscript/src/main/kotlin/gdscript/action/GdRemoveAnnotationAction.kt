package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace

class GdRemoveAnnotationAction : BaseIntentionAction {

    val element: PsiElement;

    constructor(element: PsiElement) {
        this.element = element;
    }

    override fun getText(): String {
        return "Remove annotation";
    }

    override fun getFamilyName(): String {
        return "Remove annotation";
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val space = element.parent?.prevSibling;
        if (space is PsiWhiteSpace) {
            space.delete();
        }
        element.delete();
    }
}