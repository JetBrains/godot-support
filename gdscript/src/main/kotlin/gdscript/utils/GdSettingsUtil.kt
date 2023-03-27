package gdscript.utils

import com.intellij.formatting.Indent
import com.intellij.psi.codeStyle.CodeStyleSettings
import gdscript.GdFileType

object GdSettingsUtil {

    fun CodeStyleSettings.indentToSpaces(indent: String): Indent {
        val whiteSpace = indent.first()
        var size = 0
        if (!whiteSpace.isWhitespace()) return Indent.getSpaceIndent(size)

        while (indent[size] == whiteSpace) {
            size++
        }

        if (whiteSpace == ' ') {
            return Indent.getSpaceIndent(size)
        }

        return Indent.getSpaceIndent(size * this.getTabSize(GdFileType))
    }

}
