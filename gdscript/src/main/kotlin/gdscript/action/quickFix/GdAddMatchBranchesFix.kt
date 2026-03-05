package gdscript.action.quickFix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.prevLeaf
import gdscript.GdScriptBundle
import gdscript.psi.GdElementFactory
import gdscript.psi.GdMatchSt
import gdscript.psi.GdTypes

/**
 * Adds new branches into a match statement
 */
class GdAddMatchBranchesFix(match: GdMatchSt, val branches: Array<String>) : LocalQuickFixOnPsiElement(match) {

    override fun getText(): String =
        GdScriptBundle.message("intention.name.add.missing.statement.branches")

    override fun getFamilyName(): String = text

    override fun isAvailable(): Boolean = true

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        val match = startElement as? GdMatchSt ?: return
        if (!hasMatchBranchContent(match)) return

        val matchIndent = calculateMatchIndent(file, match)
        val branchIndent = matchIndent + '\t'
        val bodyIndent = calculateBodyIndent(match) ?: return

        val trimmed = matchIndent + match.text.trimEnd()
        val newBranches = branches.joinToString("") { "\n${branchIndent}$it:\n${bodyIndent}pass" }

        val newMatch = GdElementFactory.matchSt(project, trimmed + newBranches)
        match.replace(newMatch)
    }

    private fun hasMatchBranchContent(match: GdMatchSt): Boolean {
        var lastElement = PsiTreeUtil.getDeepestLast(match)
        while (lastElement.elementType == TokenType.WHITE_SPACE || lastElement.elementType == GdTypes.COMMENT) {
            lastElement = lastElement.prevLeaf(true) ?: return false
        }
        return true
    }

    private fun calculateMatchIndent(file: PsiFile, match: GdMatchSt): String =
        file.text.substring(0, match.textRange.startOffset).takeLastWhile { it == '\t' }

    private fun calculateBodyIndent(match: GdMatchSt): String? =
        match.matchBlockList.lastOrNull()
            ?.stmtOrSuite?.text
            ?.dropWhile { it == '\n' }
            ?.takeWhile { it == '\t' }
}
