package gdscript.inspection.fixes

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class GdRemoveElementFix(element: PsiElement, private val text: String?) : LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String {
        return this.text ?: "Remove element"
    }

    override fun getText(): String {
        return this.text ?: "Remove element"
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        startElement.delete()
    }

}
