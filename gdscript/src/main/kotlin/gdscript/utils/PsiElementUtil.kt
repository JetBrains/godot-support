package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiEditorUtil
import com.intellij.refactoring.suggested.startOffset

object PsiElementUtil {

    fun PsiElement.precedingNewLines(): Int {
        val parent = this.parent ?: return 0;
        val editor = PsiEditorUtil.findEditor(this)!!;
        val caretOffset = editor.caretModel.offset;

        val parstart = parent.startOffset;
        val tt = parent.text;
        var ending= this.textRangeInParent.endOffset;
        val total = tt.length;

        val from = this.textRangeInParent.endOffset;
        val to = caretOffset - parent.startOffset;

        val ra = this.textRangeInParent

        val partial = parent.text.substring(0, caretOffset - parent.startOffset);

        val mytt = this.text
        val myttlen = this.text.length
//        val text = parent.text.substring(0, this.textRangeInParent.endOffset);
        val text = parent.text.substring(from, maxOf(caretOffset - parent.startOffset, from));

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