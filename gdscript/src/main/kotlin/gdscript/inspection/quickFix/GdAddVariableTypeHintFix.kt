package gdscript.inspection.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*

class GdAddVariableTypeHintFix(element: PsiElement, private val desired: String, private val fullType: Boolean) : LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String = "Add variable type"
    override fun getText(): String = "Specify variable type [$desired]"

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        if (fullType) fullType(project)
        else shortType(project)
    }

    private fun fullType(project: Project) {
        val typeHint = GdElementFactory.typed(project, desired)

        endElement.addAfter(typeHint, typedPosition(startElement))
    }

    private fun shortType(project: Project) {
        val assigment = PsiTreeUtil.getChildOfType(startElement, GdAssignTyped::class.java) ?: return

        assigment.replace(GdElementFactory.shortAssignTyped(project))
    }

    private fun typedPosition(element: PsiElement): PsiElement? {
        return when (element) {
            is GdClassVarDeclTl -> element.varNmi
            is GdConstDeclTl -> element.varNmi
            is GdVarDeclSt -> element.varNmi
            is GdConstDeclSt -> element.varNmi
            else -> null
        }
    }

}
