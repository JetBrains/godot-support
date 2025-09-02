package gdscript.inspection

import GdScriptBundle.message
import com.intellij.application.options.CodeStyle
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.util.DocumentUtil
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.inspection.fixes.GdFixIndentsQuickFix

/**
 * Inspection that checks for leading whitespaces that differ from expected ones in GdScript files.
 */
class GdWhitespaceLocalInspectionTool : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return WhitespaceVisitor(holder)
    }

    private class WhitespaceVisitor(
        private val myHolder: ProblemsHolder
    ) : PsiElementVisitor() {

        override fun visitFile(file: PsiFile) {
            super.visitFile(file)

            if (file.fileType !is GdFileType) {
                return
            }

            val document = file.viewProvider.document ?: return
            val settings = CodeStyle.getSettings(file)
            val indentOptions = settings.getIndentOptionsByFile(file)
            val useTabs = indentOptions.USE_TAB_CHARACTER

            val lineCount = document.lineCount
            for (lineIndex in 0 until lineCount) {
                val startOffset = document.getLineStartOffset(lineIndex)
                val indent: CharSequence = DocumentUtil.getIndent(document, startOffset)
                val indentRange = TextRange(startOffset, startOffset + indent.length)

                if (indent.contains('\t') && !useTabs)
                    registerError(file, indentRange, true)
                if (indent.contains(' ') && useTabs)
                    registerError(file, indentRange, false)
            }
        }

        private fun registerError(file: PsiFile, range: TextRange, tab: Boolean) {
            val description = if (tab)
                message("inspections.whitespace.spaces.problem")
            else
                message("inspections.whitespace.tabs.problem")

            myHolder.registerProblem(
                file,
                description,
                ProblemHighlightType.GENERIC_ERROR,
                range,
                GdFixIndentsQuickFix()
            )
        }
    }
}