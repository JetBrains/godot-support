package gdscript.inspection.fixes

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdScriptBundle
import org.jetbrains.annotations.Nls

class GdRemoveElementFix(element: PsiElement, @param:Nls private val text: String?) : LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String {
        return this.text ?: GdScriptBundle.message("inspection.fix.remove.element.family.default")

    }

    override fun getText(): String {
        return this.text ?: GdScriptBundle.message("inspection.fix.remove.element.text.default")
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        startElement.delete()
    }

}
