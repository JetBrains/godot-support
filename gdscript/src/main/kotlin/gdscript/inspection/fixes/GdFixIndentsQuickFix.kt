package gdscript.inspection.fixes

import com.intellij.application.options.CodeStyle
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.modcommand.ModCommand
import com.intellij.modcommand.ModCommandQuickFix
import com.intellij.openapi.editor.ConvertIndentsUtil
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.util.DocumentUtil
import gdscript.GdScriptBundle

/**
 * Quick fix: Convert leading indentation on each line to match Code Style (tabs vs spaces).
 * For spaces->tabs, sub-tab remainder spaces are dropped.
 */
class GdFixIndentsQuickFix : ModCommandQuickFix() {
    override fun getName(): String = GdScriptBundle.message("inspection.whitespace.fix.indents.fix.name")
    override fun getFamilyName(): String = name
    override fun availableInBatchMode(): Boolean = false

    override fun perform(project: Project, descriptor: ProblemDescriptor): ModCommand =
        ModCommand.psiUpdate(descriptor.psiElement.containingFile) { file, _ ->
            val indentOptions = CodeStyle.getSettings(file).indentOptions
            if (indentOptions.USE_TAB_CHARACTER) {
                ConvertIndentsUtil.convertIndentsToTabs(file.fileDocument, indentOptions.TAB_SIZE, file.textRange)
                stripSpacesFromIndents(file.fileDocument)
            } else {
                ConvertIndentsUtil.convertIndentsToSpaces(file.fileDocument, indentOptions.TAB_SIZE, file.textRange)
            }
        }

    // convertIndentsToTabs leaves sub-tab remainder spaces in place; GDScript requires
    // pure-tab indents, so drop any surviving spaces from each line's indent.
    private fun stripSpacesFromIndents(document: Document) {
        DocumentUtil.executeInBulk(document) {
            for (line in 0 until document.lineCount) {
                val lineStart = document.getLineStartOffset(line)
                val indent = DocumentUtil.getIndent(document, lineStart)
                if (indent.contains(' ')) {
                    val cleaned = indent.toString().replace(" ", "")
                    document.replaceString(lineStart, lineStart + indent.length, cleaned)
                }
            }
        }
    }
}
