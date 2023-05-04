package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.elementType
import gdscript.psi.GdTypes

object GdCommentUtil {

    val DESCRIPTION = "desc"
    val PARAMETER = "param"
    val BRIEF_DESCRIPTION = "brief"
    val ENUM = "enum"
    val RETURN = "return"

    fun collectAllDescriptions(element: PsiElement?): Map<String, List<String>> {
        val descriptions = mutableMapOf<String, MutableList<String>>()
        descriptions[DESCRIPTION] = mutableListOf()
        descriptions[BRIEF_DESCRIPTION] = mutableListOf()
        descriptions[PARAMETER] = mutableListOf()
        descriptions[ENUM] = mutableListOf()
        descriptions[RETURN] = mutableListOf()

        var el = element?.prevSibling
        while (el != null) {
            when (el.elementType) {
                GdTypes.COMMENT -> {
                    val text = el.text.removePrefix("#").trimStart()
                    val prefix = text.substringBefore(" ")
                    (descriptions[prefix.trim()] ?: descriptions[DESCRIPTION]!!).add(0, text.substringAfter(" "))
                }

                TokenType.WHITE_SPACE -> {}
                GdTypes.ANNOTATION_TL -> {}
                else -> break
            }
            el = el.prevSibling
        }

        return descriptions
    }

}
