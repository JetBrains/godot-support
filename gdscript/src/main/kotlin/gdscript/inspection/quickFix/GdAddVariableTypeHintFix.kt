package gdscript.inspection.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAssignTyped
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdElementFactory
import gdscript.psi.GdVarDeclSt

class GdAddVariableTypeHintFix : LocalQuickFixOnPsiElement {

    private val desired: String
    private val fullType: Boolean

    constructor(element: PsiElement, desired: String, fullType: Boolean) : super(element) {
        this.desired = desired
        this.fullType = fullType
    }

    override fun getFamilyName(): String = "Add return type"

    override fun getText(): String = "Specify return type [$desired]"

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        if (fullType) fullType(project, file, startElement, endElement)
        else shortType(project, file, startElement, endElement)
    }

    private fun fullType(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val typeHint = GdElementFactory.typed(project, desired)

        endElement.addAfter(typeHint, typedPosition(startElement))
    }

    private fun shortType(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val assigment = PsiTreeUtil.getChildOfType(endElement, GdAssignTyped::class.java) ?: return

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
