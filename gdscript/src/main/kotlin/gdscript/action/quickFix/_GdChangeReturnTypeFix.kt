package gdscript.action.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.GdElementFactory
import gdscript.psi.GdReturnHintVal

/**
 * Updates return type of function "func asd() -> CHANGE_TYPE:"
 */
class _GdChangeReturnTypeFix : LocalQuickFixOnPsiElement {

    private val element: GdReturnHintVal
    private val desired: String

    constructor(element: GdReturnHintVal, desired: String): super(element) {
        this.element = element
        this.desired = desired
    }

    override fun getFamilyName(): String {
        return "Change return type to $desired"
    }

    override fun getText(): String {
        return "Change return type to $desired"
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        element.replace(GdElementFactory.returnHintVal(project, desired))
    }

}
