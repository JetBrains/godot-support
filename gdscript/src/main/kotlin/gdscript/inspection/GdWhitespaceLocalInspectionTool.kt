package gdscript.inspection

import GdScriptBundle.message
import com.intellij.application.options.CodeStyle
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.jetbrains.rider.godot.community.gdscript.GdFileType

/**
 * Inspection that checks for leading whitespaces that differ from expected ones in GdScript files.
 */
class GdWhitespaceLocalInspectionTool : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return WhitespaceVisitor(holder)
    }

    private inner class WhitespaceVisitor(
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
            for (i in 0 until lineCount) {
                val startOffset = document.getLineStartOffset(i)
                val endOffset = document.getLineEndOffset(i)
                val line = document.getText(TextRange(startOffset, endOffset))

                // Iterate only through the leading whitespace characters of the line
                for (j in 0 until line.length) {
                    val c = line[j]

                    // If we encounter a non-whitespace character, the leading whitespace sequence has ended
                    if (c != ' ' && c != '\t') {
                        break
                    }

                    if (useTabs) {
                        // If tabs are preferred, but we find a space in the leading whitespace
                        if (c == ' ') {
                            registerError(file, startOffset + j, true) // 'true' for space problem
                        }
                    } else {
                        // If spaces are preferred, but we find a tab in the leading whitespace
                        if (c == '\t') {
                            registerError(file, startOffset + j, false) // 'false' for tab problem
                        }
                    }
                }
            }
        }

        private fun registerError(file: PsiFile, offset: Int, tab: Boolean) {
            val element = file.findElementAt(offset)
            if (element != null && isSuppressedFor(element)) {
                return
            }

            val description = if (tab)
                message("inspections.whitespace.spaces.problem")
            else
                message("inspections.whitespace.tabs.problem")

            val range = TextRange(offset, offset + 1)

                myHolder.registerProblem(
                    file,
                    description,
                    ProblemHighlightType.GENERIC_ERROR,
                    range,
                    // ReformatQuickFix, I don't like how it looks. there is "apply for all similar errors in File", it doesn't make sense.
                )
        }
    }
}