package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.refactoring.suggested.startOffset

object PsiElementUtil {

    fun PsiElement.precedingNewLines(): Int {
        val parent = this.parent ?: return 0;
        val editor = PsiEditorUtil.findEditor(this)!!;
        val caretOffset = editor.caretModel.offset;

        if (parent.text.isEmpty()) return 0;
        val partial = parent.text.substring(0, minOf(caretOffset - parent.startOffset, parent.text.length));

        var c = 0;
        for (ch in partial.toCharArray().reversed()) {
            if (ch == '\n') {
                c += 1;
            } else if (ch != ' ') {
                break;
            }
        }

        return c;
    }

}