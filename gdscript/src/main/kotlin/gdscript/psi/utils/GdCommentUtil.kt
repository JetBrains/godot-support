package gdscript.psi.utils

import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBasedPsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.elementType
import gdscript.codeInsight.documentation.GdDocUtil
import gdscript.codeInsight.documentation.GdGodotDocUtil
import gdscript.lineMarker.GdTraitLineMarkerContributor
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdClassNaming
import gdscript.psi.GdTypes
import gdscript.psi.types.GdDocumented
import gdscript.utils.PsiElementUtil.prevCommentBlock

object GdCommentUtil {

    val TUTORIAL_REGEX = "@tutorial(?:\\((.+)\\))?:\\s+(.+)".toRegex()

    val DESCRIPTION = "desc"
    val PARAMETER = "param"
    val BRIEF_DESCRIPTION = "brief"
    val ENUM = "enum"
    val RETURN = "return"
    val TUTORIAL = "tutorial"
    val DEPRECATED = "deprecated"
    val EXPERIMENTAL = "experimental"

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

    private fun startsWithTag(line: String): Boolean {
        if (!line.startsWith("@"))
            return false
        val text = line.removePrefix("@")
        return (text.startsWith(BRIEF_DESCRIPTION.plus(":"))
            || text.startsWith(DESCRIPTION.plus(":"))
            || text.startsWith(PARAMETER.plus(":"))
            || text.startsWith(TUTORIAL)
            || text.startsWith(ENUM.plus(":"))
            || text.startsWith(RETURN.plus(":"))
            || text.startsWith(DEPRECATED)
            || text.startsWith(EXPERIMENTAL)
            )
    }

    fun collectComments(element: PsiElement?): GdCommentModel {
        val comments = mutableListOf<String>()
        val model = GdCommentModel()

        if (element is GdClassNaming || element is PsiFile) {
            var file = element
            if (element is GdClassNaming) file = element.parent

            var child = file?.firstChild
            var isComment = false
            var newLined = false
            while (child != null) {
                if (child.elementType == GdTypes.COMMENT) {
                    if (child.text.startsWith("##")) {
                        isComment = true
                        comments.add(child.text.removePrefix("##").trim())
                        newLined = false
                    }
                } else if (child.elementType == TokenType.WHITE_SPACE) {
                    if (child.text == "\n") {
                        if (newLined) break
                        newLined = true
                    }
                } else if (isComment) {
                    break
                }
                child = child.nextSibling
            }
        } else {
            var previous: PsiElement? = element
            var isComment = false
            while (true) {
                previous = previous?.prevCommentBlock()
                if (previous != null) {
                    val txt = previous.text
                    if (txt.startsWith("##")) {
                        comments.add(txt.removePrefix("##").trim())
                        isComment = true
                    } else break
                } else break
            }
            if (isComment) comments.reverse()
        }

        var isBrief = true
        val brief = mutableListOf<String>()
        val description = mutableListOf<String>()
        comments.forEach {
            if (startsWithTag(it)) {
                val text = it.removePrefix("@")
                if (text.startsWith(BRIEF_DESCRIPTION)) {
                    val content = text.removePrefix(BRIEF_DESCRIPTION.plus(":")).trim()
                    if (content.isNotEmpty()) {
                        brief.add(content)
                        description.add(content)
                    }
                } else if (text.startsWith(DESCRIPTION)) {
                    val content = text.removePrefix(DESCRIPTION.plus(":")).trim()
                    if (content.isNotEmpty()) {
                        description.add(content)
                    }
                } else if (text.startsWith(PARAMETER)) {
                    description.add(it)
                } else if (text.startsWith(ENUM)) {
                    description.add(it)
                } else if (text.startsWith(RETURN)) {
                    description.add(it)
                } else if (text.startsWith(DEPRECATED)) {
                    model.isDeprecated = true
                    description.add(it)
                } else if (text.startsWith(EXPERIMENTAL)) {
                    model.isExperimental = true
                    description.add(it)
                } else if (text.startsWith(TUTORIAL)) {
                    val groups = TUTORIAL_REGEX.find(it)?.groups
                    val tutorial = GdTutorial()
                    if (groups?.get(2) != null) {
                        tutorial.url = groups[2]!!.value
                        tutorial.name = groups[1]?.value ?: groups[2]!!.value
                        model.tutorials.add(tutorial)
                    }
                    description.add(it)
                }

                isBrief = false
            } else if (isBrief && it.isNotEmpty()) {
                brief.add(it)
                description.add(it)
            } else if(isBrief && it.isEmpty()) {
                if (brief.isNotEmpty()) isBrief = false
                if (description.isNotEmpty()) description.add(it)
            } else {
                description.add(it)
            }
        }

        model.brief = brief.joinToString("\n")
        model.description = description.joinToString("\n")
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
                        text = GdGodotDocUtil.parseStyles(text)
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
