package gdscript.refactoring.introduce.inplace

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
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
            val gdVar = PsiTreeUtil.findChildOfType(varDeclSt, GdVarNmi::class.java)
            val exprContainingSuite = PsiTreeUtil.getParentOfType(expr, GdSuite::class.java)
            if (varDeclSt != null && exprContainingSuite != null) {
                // TODO we need to add a newline after the variable declaration - is there an easy way?
                exprContainingSuite.addBefore(varDeclSt, expr)
            }
            gdVar
        }
        localVariable = gdVar
        return gdVar
    }

    // see https://plugins.jetbrains.com/docs/intellij/modifying-psi.html
    private fun createLocalVarDeclSt(varname: String): GdVarDeclSt? {
        val simpleDecl = """
                func a():
                    var $varname = ${expr.text}
            """.trimIndent()
        val dummyFile = PsiFileFactory.getInstance(project).createFileFromText("dummy.gd", GdLanguage, simpleDecl)
        return PsiTreeUtil.findChildOfType(dummyFile.firstChild, GdVarDeclSt::class.java)
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