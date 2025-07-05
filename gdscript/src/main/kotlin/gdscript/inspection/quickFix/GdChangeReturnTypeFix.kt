package gdscript.inspection.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.GdElementFactory
import gdscript.psi.GdReturnHintVal

/**
 * Updates return type of function "func asd() -> CHANGE_TYPE:"
 */
class GdChangeReturnTypeFix(element: GdReturnHintVal, private val desired: String): LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String = "Change return type"
    override fun getText(): String = "Change return type to $desired"

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        startElement.replace(GdElementFactory.returnHintVal(project, desired))
    }

}
