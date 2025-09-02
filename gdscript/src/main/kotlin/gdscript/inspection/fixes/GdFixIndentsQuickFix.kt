package gdscript.inspection.fixes

import GdScriptBundle.message
import com.intellij.application.options.CodeStyle
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.modcommand.ModCommand
import com.intellij.modcommand.ModCommandQuickFix
import com.intellij.modcommand.ModPsiUpdater
import com.intellij.openapi.editor.ConvertIndentsUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Quick fix: Convert leading indentation on each line to match Code Style (tabs vs spaces).
 * Delegates to platform ConvertIndentsUtil for consistent behavior.
 */
class GdFixIndentsQuickFix : ModCommandQuickFix() {
    override fun getFamilyName(): String = getName()
    override fun getName(): String = message("inspections.whitespace.fix.indents.fix.name")
    override fun availableInBatchMode(): Boolean = false

    override fun perform(
        project: Project,
        descriptor: ProblemDescriptor,
    ): ModCommand {
        return ModCommand.psiUpdate(descriptor.psiElement.containingFile) { mutableFile: PsiFile, _: ModPsiUpdater ->
            val indentOptions = CodeStyle.getSettings(mutableFile).getIndentOptionsByFile(mutableFile.containingFile)
            val range = mutableFile.containingFile.textRange
            if (indentOptions.USE_TAB_CHARACTER) {
                ConvertIndentsUtil.convertIndentsToTabs(mutableFile.fileDocument, indentOptions.TAB_SIZE, range)
            }
            else {
                ConvertIndentsUtil.convertIndentsToSpaces(mutableFile.fileDocument, indentOptions.TAB_SIZE, range)
            }
        }
    }
}
