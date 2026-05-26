package gdscript.inspection.fixes

import com.intellij.application.options.CodeStyle
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.modcommand.ModCommand
import com.intellij.modcommand.ModCommandQuickFix
import com.intellij.modcommand.ModPsiUpdater
import com.intellij.openapi.editor.ConvertIndentsUtil
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.util.DocumentUtil
import gdscript.GdScriptBundle

/**
 * Quick fix: Convert leading indentation on each line to match Code Style (tabs vs spaces).
 * For spaces->tabs, sub-tab remainder spaces are dropped.
 */
class GdFixIndentsQuickFix : ModCommandQuickFix() {
    override fun getFamilyName(): String = getName()
    override fun getName(): String = GdScriptBundle.message("inspection.whitespace.fix.indents.fix.name")
    override fun availableInBatchMode(): Boolean = false

    override fun perform(
        project: Project,
        descriptor: ProblemDescriptor,
    ): ModCommand {
        return ModCommand.psiUpdate(descriptor.psiElement.containingFile) { mutableFile: PsiFile, _: ModPsiUpdater ->
            val indentOptions = CodeStyle.getSettings(mutableFile).getIndentOptionsByFile(mutableFile.containingFile)
            val range = mutableFile.containingFile.textRange
            val document = mutableFile.fileDocument
            if (indentOptions.USE_TAB_CHARACTER) {
                ConvertIndentsUtil.convertIndentsToTabs(document, indentOptions.TAB_SIZE, range)
                stripSpacesFromIndents(document, range)
            }
            else {
                ConvertIndentsUtil.convertIndentsToSpaces(mutableFile.fileDocument, indentOptions.TAB_SIZE, range)
            }
        }
    }

    private fun stripSpacesFromIndents(document: Document, textRange: TextRange) {
        DocumentUtil.executeInBulk(document) {
            val startLine = document.getLineNumber(textRange.startOffset)
            val endLine = document.getLineNumber(textRange.endOffset)
            for (line in startLine..endLine) {
                val lineStart = document.getLineStartOffset(line)
                val indent = DocumentUtil.getIndent(document, lineStart)
                if (indent.contains(' ')) {
                    val cleaned = indent.filter { it != ' ' }.toString()
                    document.replaceString(lineStart, lineStart + indent.length, cleaned)
                }
            }
        }
    }
}
