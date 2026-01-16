package gdscript.inspection.fixes

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdScriptBundle
import gdscript.psi.GdElementFactory
import gdscript.psi.GdReturnHintVal

/**
 * Updates return type of function "func asd() -> CHANGE_TYPE:"
 */
class GdChangeReturnTypeFix(element: GdReturnHintVal, private val desired: String): LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String = GdScriptBundle.message("inspection.fix.change.return.type.family")
    override fun getText(): String = GdScriptBundle.message("inspection.fix.change.return.type.text", desired)

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        startElement.replace(GdElementFactory.returnHintVal(project, desired))
    }

}
