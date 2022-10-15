package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.refactoring.suggested.startOffset

object PsiElementUtil {

    fun PsiElement.precedingNewLines(): Int {
        val parent = this.parent ?: return 0;
        val editor = PsiEditorUtil.findEditor(this)!!;
        val caretOffset = editor.caretModel.offset;

        val partial = parent.text.substring(0, caretOffset - parent.startOffset);

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