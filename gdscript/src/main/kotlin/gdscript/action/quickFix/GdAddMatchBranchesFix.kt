package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.editor.actions.EditorActionUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.prevLeaf
import gdscript.action.util.IndentUtil.indents
import gdscript.psi.GdMatchSt
import gdscript.psi.GdTypes

/**
 * Adds new branches into a match statement
 */
class GdAddMatchBranchesFix(val match: GdMatchSt, val branches: Array<String>) : BaseIntentionAction() {

    override fun getText(): String {
        return GdScriptBundle.message("intention.name.add.missing.statement.branches")
    }

    override fun getFamilyName(): String {
        return GdScriptBundle.message("intention.name.add.missing.statement.branches")
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) return

        val indentSize = editor.settings.getTabSize(project)
        val indents = editor.indents(match) + indentSize

        var lastElement = PsiTreeUtil.getDeepestLast(match)
        while (lastElement.elementType == TokenType.WHITE_SPACE || lastElement.elementType == GdTypes.COMMENT) {
            lastElement = lastElement.prevLeaf(true) ?: return
        }

        val end = lastElement.endOffset
        val caret = editor.caretModel

        val psiManager = PsiDocumentManager.getInstance(project)
        var lineNumber = editor.document.getLineNumber(end)

        psiManager.doPostponedOperationsAndUnblockDocument(editor.document)
        caret.moveToOffset(end)

        branches.forEach {
            EditorModificationUtil.insertStringAtCaret(editor, "\n")
            EditorActionUtil.indentLine(project, editor, ++lineNumber, indents)
            EditorModificationUtil.insertStringAtCaret(editor, "$it:\n")
            EditorActionUtil.indentLine(project, editor, ++lineNumber, indents + indentSize)
            EditorModificationUtil.insertStringAtCaret(editor, "pass")
        }

        psiManager.commitDocument(editor.document)
    }

}
