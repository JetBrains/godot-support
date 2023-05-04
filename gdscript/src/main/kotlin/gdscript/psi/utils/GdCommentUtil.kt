package gdscript.psi.utils

import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.elementType
import gdscript.codeInsight.documentation.GdDocUtil
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
                    var text = el.text.removePrefix("#").trimStart()
                    var prefix = text.substringBefore(" ")

                    if (!descriptions.containsKey(prefix)) prefix = DESCRIPTION
                    else text = text.substringAfter(" ")

                    (descriptions[prefix]!!).add(0, text)
                }

                TokenType.WHITE_SPACE -> {}
                GdTypes.ANNOTATION_TL -> {}
                else -> break
            }
            el = el.prevSibling
        }

        return descriptions
    }

    fun Map<String, List<String>>.briefDescriptionBlock(): HtmlChunk {
        val comments = if (this[BRIEF_DESCRIPTION]!!.isNotEmpty()) this[BRIEF_DESCRIPTION] else this[DESCRIPTION]
        return GdDocUtil.paragraph(comments!!)
    }

    fun Map<String, List<String>>.descriptionBlock(): HtmlChunk {
        return GdDocUtil.paragraph(this[DESCRIPTION]!!)
    }

    fun Map<String, List<String>>.parameterBlock(): HtmlChunk {
        return GdDocUtil.listTable("params", this[PARAMETER]!!.map {
            it.replaceFirst(" ", " - ")
        })
    }

    fun Map<String, List<String>>.returnBlock(): HtmlChunk {
        return GdDocUtil.listTable("return", this[RETURN]!!)
    }

}
