package gdscript.inspection.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import gdscript.psi.GdElementFactory
import gdscript.psi.GdNamedIdElement
import gdscript.psi.GdTypes

class GdAddReturnTypeHintFix(element: GdNamedIdElement, private val desired: String) : LocalQuickFixOnPsiElement(element) {

    override fun getFamilyName(): String = "Add return type"
    override fun getText(): String = "Specify return type [$desired]"

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val rrbrElem = findRRBRElem()
        if (rrbrElem != null) {
            val returnHint = GdElementFactory.returnHint(project, desired)
            // prevSibling refers to a whitespace
            startElement.parent.addRangeAfter(returnHint.prevSibling, returnHint, rrbrElem)
        }
    }

    private fun findRRBRElem(): PsiElement? {
        var maybeElement = startElement.nextSibling
        while (maybeElement != null) {
            if (maybeElement.elementType == GdTypes.RRBR) {
                return maybeElement
            }
            maybeElement = maybeElement.nextSibling
        }
        return null
    }
}
