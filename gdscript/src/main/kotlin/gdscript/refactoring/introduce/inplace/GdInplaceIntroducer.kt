package gdscript.refactoring.introduce.inplace

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.introduce.inplace.AbstractInplaceIntroducer
import gdscript.GdLanguage
import gdscript.psi.GdExpr
import gdscript.psi.GdSuite
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi
import javax.swing.JComponent

class GdInplaceIntroducer(
    val project: Project?,
    val editor: Editor?,
    val expr: GdExpr,
    var localVariable: GdVarNmi?,
    occurrences: Array<out GdExpr>?, title: @NlsContexts.Command String?,
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
        return localVariable
    }

    override fun createFieldToStartTemplateOn(replaceAll: Boolean, names: Array<out String>): GdVarNmi? {
        // create write action otherwise the concurrency model will complain
        val gdVar = WriteAction.computeAndWait<GdVarNmi, Exception> {
            val varDeclSt = createLocalVarDeclSt(names[0])
            val exprContainingSuite = PsiTreeUtil.getParentOfType(expr, GdSuite::class.java)
            if (varDeclSt != null && exprContainingSuite != null) {
                val insertionPoint = findInsertionPoint(expr)
                val added = exprContainingSuite.addBefore(varDeclSt, insertionPoint)
                PsiTreeUtil.findChildOfType(added, GdVarNmi::class.java)
            } else {
                null
            }
        }
        myLocalVariable = gdVar // update the local variable for the superclass
        return gdVar
    }

    // see https://plugins.jetbrains.com/docs/intellij/modifying-psi.html
    private fun createLocalVarDeclSt(varname: String): GdVarDeclSt? {
        // this declaration needs a newline afte the assignment, thus the dummy statement
        val simpleDecl = """
                func a():
                    var $varname = ${expr.text}
                    var b = 1
            """.trimIndent()
        val dummyFile = PsiFileFactory.getInstance(project).createFileFromText("dummy.gd", GdLanguage, simpleDecl)
        return PsiTreeUtil.findChildOfType(dummyFile.firstChild, GdVarDeclSt::class.java)
    }

    private fun findInsertionPoint(el: PsiElement): PsiElement {
        if (el.parent is GdSuite) {
            return el
        }
        return findInsertionPoint(el.parent)
    }

    override fun getComponent(): JComponent? {
        return null
    }

    override fun performIntroduce() {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun suggestNames(replaceAll: Boolean, variable: GdVarNmi?): Array<String> {
        return arrayOf("foo") // TODO
    }
}