package gdscript.inspection.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.GdNamedElement

class GdAddUnderscoreFix : LocalQuickFixOnPsiElement {

    constructor(element: PsiElement): super(element)

    override fun getFamilyName(): String {
        return "Ignore unused"
    }

    override fun getText(): String {
        return "Ignore unused"
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val el = if (startElement is GdNamedElement) startElement else startElement.firstChild
        if (el is GdNamedElement) {
            el.setName("_${el.name}")
        }
    }

}
