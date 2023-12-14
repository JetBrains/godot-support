package gdscript.inspection.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class GdRemoveElementFix : LocalQuickFixOnPsiElement {

    private val element: PsiElement
    private val text: String?

    constructor(element: PsiElement, text: String? = null) : super(element) {
        this.element = element
        this.text = text
    }

    override fun getFamilyName(): String {
        return this.text ?: "Remove element"
    }

    override fun getText(): String {
        return this.text ?: "Remove element"
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        element.delete()
    }

}
