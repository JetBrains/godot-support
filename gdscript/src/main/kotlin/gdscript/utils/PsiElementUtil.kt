package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.psi.GdCallEx
import gdscript.psi.GdTypes

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

    fun PsiElement.getCallExpr(): GdCallEx? {
        val next = PsiTreeUtil.nextVisibleLeaf(this) ?: return null
        if (next.elementType == GdTypes.LRBR && next.parent?.elementType == GdTypes.CALL_EX) {
            return next.parent as GdCallEx
        }

        return null
    }

}
