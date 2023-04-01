package gdscript.utils

import com.intellij.formatting.Indent
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import gdscript.GdFileType

object GdSettingsUtil {

    fun CodeStyleSettings.indentToSpaces(indent: String, continuation: Boolean = false): Indent {
        val whiteSpace = indent.first()
        val tabSize = this.getTabSize(GdFileType)
        val baseSize = if (continuation) tabSize else 0
        var size = 0
        if (!whiteSpace.isWhitespace()) return Indent.getSpaceIndent(baseSize)

        while (indent[size] == whiteSpace) {
            size++
        }

        if (whiteSpace == ' ') {
            return Indent.getSpaceIndent(size + baseSize)
        }

        return Indent.getSpaceIndent(size * tabSize + baseSize)
    }

    fun CodeStyleSettings.calculateSpaceIndents(element: PsiElement, continuation: Boolean = false): Indent {
        val lastVisible = PsiTreeUtil.getDeepestVisibleLast(element) ?: element
        val document = PsiEditorUtil.findEditor(lastVisible)!!.document
        val line = document.getLineNumber(lastVisible.endOffset)
        val indentedLine =
            document.text.substring(document.getLineStartOffset(line), document.getLineEndOffset(line))

        return this.indentToSpaces(indentedLine, continuation)
    }

}
