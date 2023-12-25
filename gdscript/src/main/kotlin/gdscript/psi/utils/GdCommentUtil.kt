package gdscript.psi.utils

import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import com.intellij.psi.StubBasedPsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.elementType
import gdscript.codeInsight.documentation.GdDocUtil
import gdscript.codeInsight.documentation.GdGodotDocUtil
import gdscript.lineMarker.GdTraitLineMarkerContributor
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdTypes
import gdscript.psi.types.GdDocumented
import gdscript.utils.PsiElementUtil.prevCommentBlock

object GdCommentUtil {

    val TUTORIAL_REGEX = "@tutorial(\\(.+\\))?:\\s+(.+)".toRegex()

    val DESCRIPTION = "desc"
    val PARAMETER = "param"
    val BRIEF_DESCRIPTION = "brief"
    val ENUM = "enum"
    val RETURN = "return"
    val TUTORIAL = "tutorial"

    val BREAKS_AT = arrayOf(
        GdTraitLineMarkerContributor.PREFIX.trimStart('#'),
        GdTraitLineMarkerContributor.SUFFIX.trimStart('#'),
    )

    fun brief(element: PsiElement): String {
        if (element is StubBasedPsiElement<*> && element is GdDocumented) {
            val stub = element.stub
            if (stub != null && stub is GdDocumented) return stub.brief()
        }

        val model = collectComments(element)
        return model.brief.ifEmpty { model.description }
    }

    fun description(element: PsiElement): String {
        if (element is StubBasedPsiElement<*> && element is GdDocumented) {
            val stub = element.stub
            if (stub != null && stub is GdDocumented) return stub.description()
        }

        val model = collectComments(element)
        return model.description
    }

    fun tutorials(element: PsiElement): List<GdTutorial> {
        if (element is StubBasedPsiElement<*> && element is GdDocumented) {
            val stub = element.stub
            if (stub != null && stub is GdDocumented) return stub.tutorials()
        }

        val model = collectComments(element)
        return model.tutorials
    }

    fun isDeprecated(element: PsiElement): Boolean {
        if (element is StubBasedPsiElement<*> && element is GdDocumented) {
            val stub = element.stub
            if (stub != null && stub is GdDocumented) return stub.isDeprecated()
        }

        val model = collectComments(element)
        return model.isDeprecated
    }

    fun isExperimental(element: PsiElement): Boolean {
        if (element is StubBasedPsiElement<*> && element is GdDocumented) {
            val stub = element.stub
            if (stub != null && stub is GdDocumented) return stub.isExperimental()
        }

        val model = collectComments(element)
        return model.isExperimental
    }

    fun collectComments(element: PsiElement?): GdCommentModel {
        val comments = mutableListOf<String>()
        val model = GdCommentModel()

        var previous: PsiElement? = element
        while (true) {
            previous = previous?.prevCommentBlock()
            if (previous != null) {
                val txt = previous.text
                if (!txt.startsWith("##")) break
                comments.add(txt.removePrefix("##").trim())
            } else break
        }

        var brief = false
        comments.forEach {
            if (it.startsWith("@description")) {
                model.isDeprecated = true
            } else if (it.startsWith("@tutorial")) {
                val groups = TUTORIAL_REGEX.find(it)?.groups
                val tutorial = GdTutorial()
                if (groups?.get(2) != null) {
                    tutorial.url = groups[2]!!.value
                    tutorial.name = groups[1]?.value ?: groups[2]!!.value
                    model.tutorials.add(0, tutorial)
                }
            } else if (it.trim() == "" && model.description != "") {
                brief = true
            } else {
                if (brief) {
                    model.brief = "${it}\n${model.brief}".trim('\n')
                } else {
                    model.description = "${it}\n${model.description}".trim('\n')
                }
            }
        }

        return model
    }

    fun collectAllDescriptions(element: PsiElement?): Map<String, List<String>> {
        val descriptions = mutableMapOf<String, MutableList<String>>()
        descriptions[DESCRIPTION] = mutableListOf()
        descriptions[BRIEF_DESCRIPTION] = mutableListOf()
        descriptions[PARAMETER] = mutableListOf()
        descriptions[ENUM] = mutableListOf()
        descriptions[RETURN] = mutableListOf()
        descriptions[TUTORIAL] = mutableListOf()

        var el = element?.prevSibling
        while (el != null) {
            when (el.elementType) {
                GdTypes.COMMENT -> {
                    var text = el.text.removePrefix("#").trimStart()
                    var prefix = text.substringBefore(" ")

                    if (BREAKS_AT.contains(prefix)) break

                    if (!descriptions.containsKey(prefix)) prefix = DESCRIPTION
                    else text = text.substringAfter(" ")
                    if (prefix != TUTORIAL) {
                        text = GdGodotDocUtil.parserDescription(text)
                    }

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
        return GdDocUtil.paragraph("")
    }

    fun Map<String, List<String>>.descriptionBlock(): HtmlChunk {
        return GdDocUtil.paragraph("")
    }

    fun Map<String, List<String>>.tutorialBlock(): HtmlChunk {
        return GdDocUtil.listTable("tutorials", this[TUTORIAL]!!.map {
            HtmlChunk.link(it.substringAfter("]").trim(), it.substringBefore("]").removePrefix("[").trim())
        })
    }

    fun Map<String, List<String>>.descriptionText(): String {
        return this[DESCRIPTION]!!.joinToString("<br/>")
    }

    fun Map<String, List<String>>.parameterBlock(): HtmlChunk {
        return GdDocUtil.listTable("params", this[PARAMETER]!!.map {
            HtmlChunk.raw(it.replaceFirst(" ", " - "))
        })
    }

    fun Map<String, List<String>>.returnBlock(): HtmlChunk {
        return GdDocUtil.listTable("return", this[RETURN]!!.map {
            HtmlChunk.raw(it)
        })
    }

}
