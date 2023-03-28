package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiEditorUtil

object PsiElementUtil {

    fun PsiElement.precedingNewLines(position: Int): Int {
        val parent = this.parent ?: return 0

        if (parent.text.isEmpty()) return 0
        val partial = parent.containingFile.text.substring(0, position)

        var c = 0
        for (ch in partial.toCharArray().reversed()) {
            if (ch == '\n') {
                c += 1
            } else if (ch != ' ' && ch != '\t') {
                break
            }
        }

        return c
    }

    fun PsiElement.getCaretOffsetIfSingle(): Int? {
        val editor = PsiEditorUtil.findEditor(this) ?: return null
        if (editor.caretModel.caretCount != 1) {
            return null
        }

        return editor.caretModel.currentCaret.offset
    }

}
