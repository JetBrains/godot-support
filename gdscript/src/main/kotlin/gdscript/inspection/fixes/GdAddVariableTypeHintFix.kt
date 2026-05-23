package gdscript.inspection.fixes

import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.psi.GdAssignTyped
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdElementFactory
import gdscript.psi.GdVarDeclSt

class GdAddVariableTypeHintFix : LocalQuickFixOnPsiElement, PriorityAction {

    private val desired: String
    private val fullType: Boolean

    constructor(element: PsiElement, desired: String, fullType: Boolean) : super(element) {
        this.desired = desired
        this.fullType = fullType
    }

    override fun getFamilyName(): String =
        if (fullType) GdScriptBundle.message("inspection.fix.add.variable.type.hint.family")
        else GdScriptBundle.message("inspection.fix.add.variable.type.hint.short.family")

    override fun getText(): String =
        if (fullType) GdScriptBundle.message("inspection.fix.add.variable.type.hint.text", desired)
        else GdScriptBundle.message("inspection.fix.add.variable.type.hint.short.text")

    override fun getPriority(): PriorityAction.Priority =
        if (fullType) PriorityAction.Priority.HIGH
        else PriorityAction.Priority.NORMAL

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        if (fullType) fullType(project, startElement, endElement)
        else shortType(project, startElement)
    }

    private fun fullType(project: Project, startElement: PsiElement, endElement: PsiElement) {
        val typeHint = GdElementFactory.typed(project, desired)

        endElement.addAfter(typeHint, typedPosition(startElement))
    }

    private fun shortType(project: Project, startElement: PsiElement) {
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
