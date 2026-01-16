package gdscript.refactoring.inline

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.refactoring.RefactoringBundle
import com.intellij.refactoring.inline.InlineOptionsDialog
import gdscript.GdScriptBundle
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

        title = RefactoringBundle.message("inline.variable.title")
        init()
    }

    override fun getNameLabelText(): String {
        val parent = myElement.parent
        val isConst = parent is GdConstDeclTl || parent is GdConstDeclSt
        val variableName: String = (myElement as GdVarNmi).name

        val text = if (isConst) GdScriptBundle.message("refactoring.inline.variable.label.constant", variableName)
        else GdScriptBundle.message("refactoring.inline.variable.label.variable", variableName)

        return if (occurrences > 1)
            "$text, ${RefactoringBundle.message("occurrences.string", occurrences)}"
        else
            text
    }

    override fun getBorderTitle(): String {
        return RefactoringBundle.message("inline.variable.title")
    }

    override fun getInlineAllText(): String {
        return RefactoringBundle.message("all.references.and.remove.the.local")
    }

    override fun getInlineThisText(): String {
        return RefactoringBundle.message("this.reference.only.and.keep.the.variable")
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
