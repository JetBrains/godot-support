package gdscript.utils

import com.intellij.formatting.Indent
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import gdscript.GdFileType
import kotlin.math.max

object GdSettingsUtil {

    fun CodeStyleSettings.indentToSpaces(indent: String, offset: Int = 0): Indent {
        val whiteSpace = indent.first()
        val tabSize = this.getTabSize(GdFileType)
        val baseSize = offset * tabSize
        var size = 0
        if (!whiteSpace.isWhitespace()) return Indent.getSpaceIndent(baseSize)

        while (indent[size] == whiteSpace) {
            size++
        }

        if (whiteSpace != ' ') {
            size *= tabSize
        }

        return Indent.getSpaceIndent(max(size + baseSize, 0))
    }

    fun CodeStyleSettings.calculateSpaceIndents(element: PsiElement, offset: Int = 0): Indent {
        val lastVisible = PsiTreeUtil.getDeepestVisibleLast(element) ?: element
        val document = PsiEditorUtil.findEditor(lastVisible)!!.document
        val line = document.getLineNumber(lastVisible.endOffset)
        val indentedLine =
            document.text.substring(document.getLineStartOffset(line), document.getLineEndOffset(line))

        return this.indentToSpaces(indentedLine, offset)
    }

}
