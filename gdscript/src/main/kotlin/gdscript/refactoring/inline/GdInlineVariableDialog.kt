package gdscript.refactoring.inline

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.refactoring.inline.InlineOptionsDialog
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdVarNmi

class GdInlineVariableDialog : InlineOptionsDialog {

    private val myRef: PsiElement?
    private val occurrences: Int

    constructor(project: Project, field: GdVarNmi, ref: PsiElement?) : super(project, true, field) {
        this.myRef = ref
        this.occurrences = getNumberOfOccurrences(field)
        myInvokedOnReference = ref != null

        title = "Inline Variable"
        init()
    }

    override fun getNameLabelText(): String {
        val parent = myElement.parent
        val isConst = parent is GdConstDeclTl || parent is GdConstDeclSt

        var label = "${if (isConst) "Constant" else "Variable"} ${(myElement as GdVarNmi).name}"

        if (occurrences > 1) {
            label += ", $occurrences occurrences"
        }

        return label
    }

    override fun getBorderTitle(): String {
        return "Inline Variable"
    }

    override fun getInlineAllText(): String {
        return "Inline all references and remove the variable"
    }

    override fun getInlineThisText(): String {
        return "Inline this reference and keep the variable"
    }

    override fun allowInlineAll(): Boolean {
        return true
    }

    override fun isInlineThis(): Boolean {
        return true
    }

    override fun ignoreOccurrence(reference: PsiReference?): Boolean {
        return true
    }

    override fun doAction() {
        invokeRefactoring(
            GdInlineVariableProcessor(project, myElement, myRef, isInlineThisOnly, !isKeepTheDeclaration())
        )
    }

    // Could not figure out how to handle it the proper way, so for single usage I overcome it by this
    fun inlineAndRemove() {
        myRbInlineAll.isSelected = true
        doAction()
    }

}
