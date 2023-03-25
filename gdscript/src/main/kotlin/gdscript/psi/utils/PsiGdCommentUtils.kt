package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.elementType
import com.intellij.util.containers.toArray
import gdscript.psi.GdTypes

object PsiGdCommentUtils {

    val DESCRIPTION = "desc";
    val BRIEF_DESCRIPTION = "brief";

    fun collectDescriptions(element: PsiElement?, key: String = DESCRIPTION): Array<String> {
        val descs = mutableListOf<String>();

        var el = element?.prevSibling;
        while (el != null) {
            when (el.elementType) {
                GdTypes.COMMENT -> {
                    val text = el.text;
                    val prefix = "#$key ";
                    if (text.startsWith(prefix)) {
                        descs.add(text.substring(prefix.length));
                    }
                }
                TokenType.WHITE_SPACE -> {}
                else -> break
            }
            el = el.prevSibling
        }

        return descs.reversed().toArray(emptyArray());
    }

}
