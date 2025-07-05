package gdscript.refactoring.introduce.inplace

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.introduce.inplace.AbstractInplaceIntroducer
import gdscript.psi.*
import javax.swing.JComponent

class GdInplaceIntroducer(
    val project: Project?,
    val editor: Editor?,
    val expr: GdExpr,
    localVariable: GdVarNmi?,
    occurrences: Array<out GdExpr>?,
    title: @NlsContexts.Command String?,
    val file: PsiFile
) : AbstractInplaceIntroducer<GdVarNmi, GdExpr>(
    project,
    editor,
    expr,
    localVariable,
    occurrences,
    title,
    file.fileType
) {
    private var replaceAllOccurrences: Boolean = false

    /**
     * NOTE: This is a hack to prevent the superclass from calling `restoreExpression` and `saveSettings` which cause exceptions for no apparant reason.
     * @see AbstractInplaceIntroducer.finish
     */
    private var isFinished: Boolean = false

    override fun getActionName(): String {
        return "Introduce Variable"
    }

    override fun isReplaceAllOccurrences(): Boolean {
        return replaceAllOccurrences
    }

    override fun setReplaceAllOccurrences(allOccurrences: Boolean) {
        replaceAllOccurrences = allOccurrences
    }

    override fun getVariable(): GdVarNmi? {
        return if (isFinished) null else myLocalVariable
    }

    override fun createFieldToStartTemplateOn(replaceAll: Boolean, names: Array<out String>): GdVarNmi? {
        // create write action otherwise the concurrency model will complain
        myLocalVariable = WriteAction.computeAndWait<GdVarNmi, Exception> {
            val varDeclSt = createLocalVarDeclSt(names[0])

            val exprContainingSuite = PsiTreeUtil.getParentOfType(expr, GdSuite::class.java)
            if (exprContainingSuite != null) {
                val insertionPoint = findInsertionPoint(expr)
                // use the added element to get the varNmi otherwise the cursor to rename will be at a random position
                val added = exprContainingSuite.addBefore(varDeclSt, insertionPoint) as GdVarDeclSt
                added.varNmi
            } else {
                null
            }
        }

        return myLocalVariable
    }

    private fun createLocalVarDeclSt(varname: String): GdVarDeclSt {
        return GdElementFactory.varDecl(project!!, varname, expr.text)
    }

    private fun findInsertionPoint(el: PsiElement): PsiElement {
        return PsiTreeUtil.getParentOfType(el, GdStmt::class.java) ?: el
    }

    override fun getComponent(): JComponent? {
        return null // unused - probably only if there are some options to set
    }

    override fun performIntroduce() {
        // unused
    }

    override fun finish(success: Boolean) {
        isFinished = true
        super.finish(success)
    }

    override fun restoreExpression(
        containingFile: PsiFile,
        variable: GdVarNmi,
        marker: RangeMarker,
        exprText: String?
    ): GdExpr {
        return expr
    }

    override fun saveSettings(variable: GdVarNmi) {
        // unused - probably only if there are some options to set
    }

    override fun suggestNames(replaceAll: Boolean, variable: GdVarNmi?): Array<String> {
        return arrayOf("foo") // TODO these are ignored in our implementation currently by the system. It has to do with the template of myLocalVariable
    }
}
