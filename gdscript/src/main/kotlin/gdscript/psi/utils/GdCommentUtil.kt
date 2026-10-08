package gdscript.psi.utils

import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBasedPsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.codeInsight.documentation.GdBBCodeRenderer
import gdscript.model.GdCommentModel
import gdscript.model.GdTutorial
import gdscript.psi.GdClassNaming
import gdscript.psi.GdTypes
import gdscript.psi.types.GdDocumented
import gdscript.utils.PsiElementUtil.prevCommentBlock
import org.jetbrains.annotations.NonNls

object GdCommentUtil {

    val TUTORIAL_REGEX: Regex = "@tutorial(?:\\((.+)\\))?:\\s+(.+)".toRegex()

    @NonNls const val DESCRIPTION: String = "desc"
    @NonNls const val PARAMETER: String = "param"
    @NonNls const val BRIEF_DESCRIPTION: String = "brief"
    @NonNls const val ENUM: String = "enum"
    @NonNls const val RETURN: String = "return"
    @NonNls const val TUTORIAL: String = "tutorial"
    @NonNls const val DEPRECATED: String = "deprecated"
    @NonNls const val EXPERIMENTAL: String = "experimental"

    private val TAG_PREFIXES = listOf(
        "$BRIEF_DESCRIPTION:", "$DESCRIPTION:", "$PARAMETER:", TUTORIAL, "$ENUM:", "$RETURN:", DEPRECATED, EXPERIMENTAL,
    )

    private inline fun <T> fromStub(element: PsiElement, get: (GdDocumented) -> T): T? {
        if (element !is StubBasedPsiElement<*> || element !is GdDocumented) return null
        return (element.stub as? GdDocumented)?.let(get)
    }

    fun brief(element: PsiElement): String =
        fromStub(element) { it.brief() }
            ?: collectComments(element).let { it.brief.ifEmpty { it.description } }

    fun description(element: PsiElement): String =
        fromStub(element) { it.description() } ?: collectComments(element).description

    fun tutorials(element: PsiElement): List<GdTutorial> =
        fromStub(element) { it.tutorials() } ?: collectComments(element).tutorials

    fun isDeprecated(element: PsiElement): Boolean =
        fromStub(element) { it.isDeprecated() } ?: collectComments(element).isDeprecated

    fun isExperimental(element: PsiElement): Boolean =
        fromStub(element) { it.isExperimental() } ?: collectComments(element).isExperimental

    private fun startsWithTag(line: String): Boolean {
        val text = line.trimStart()
        if (!text.startsWith("@"))
            return false
        val tag = text.removePrefix("@")
        return TAG_PREFIXES.any { tag.startsWith(it) }
    }

    /**
     * Collects the raw `##` comment PSI leaves that make up the doc comment block for [element].
     * For a script/class header ([GdClassNaming] or [PsiFile]) it scans forward from the start of the file;
     * for any other element it scans backward from [element] using [prevCommentBlock].
     * A blank line between comment lines stops the block, matching [collectComments].
     */
    fun collectCommentNodes(element: PsiElement?): List<PsiComment> {
        val comments = mutableListOf<PsiComment>()

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
                        comments.add(child as PsiComment)
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
                        comments.add(previous as PsiComment)
                        isComment = true
                    } else break
                } else break
            }
            if (isComment) comments.reverse()
        }

        return comments
    }

    fun collectComments(element: PsiElement?): GdCommentModel {
        val comments = collectCommentNodes(element).map { stripDocCommentPrefix(it.text) }
        return parseCommentModel(comments)
    }

    /** Builds a [GdCommentModel] straight from a list of `##` comment PSI leaves, e.g. from a [gdscript.codeInsight.documentation.GdVirtualDocComment]. */
    fun collectComments(comments: List<PsiComment>): GdCommentModel {
        return parseCommentModel(comments.map { stripDocCommentPrefix(it.text) })
    }

    /**
     * Removes the `##` doc marker and one optional following space.
     * Keeps leading indent so code samples inside comments stay formatted.
     */
    fun stripDocCommentPrefix(raw: String): String {
        var text = raw
        if (text.startsWith("##")) text = text.substring(2)
        if (text.startsWith(" ")) text = text.substring(1)
        return text.trimEnd()
    }

    /**
     * Groups every `##` comment in [file] into contiguous doc-comment blocks, separated at blank lines,
     * in document order. Used by Reader Mode to enumerate/locate doc comments regardless of the element they document.
     */
    fun commentBlocks(file: PsiFile): List<List<PsiComment>> {
        val docComments = PsiTreeUtil.findChildrenOfType(file, PsiComment::class.java).filter { it.text.startsWith("##") }
        val blocks = mutableListOf<MutableList<PsiComment>>()
        docComments.forEach { comment ->
            val prev = comment.prevCommentBlock()
            if (prev is PsiComment && blocks.isNotEmpty() && blocks.last().last() == prev) {
                blocks.last().add(comment)
            } else {
                blocks.add(mutableListOf(comment))
            }
        }
        return blocks
    }

    fun parseCommentModel(comments: List<String>): GdCommentModel {
        val model = GdCommentModel()
        var isBrief = true
        val brief = mutableListOf<String>()
        val description = mutableListOf<String>()
        comments.forEach {
            if (startsWithTag(it)) {
                val text = it.trimStart().removePrefix("@")
                when {
                    text.startsWith(BRIEF_DESCRIPTION) -> {
                        val content = text.removePrefix("$BRIEF_DESCRIPTION:").trim()
                        if (content.isNotEmpty()) {
                            brief.add(content)
                            description.add(content)
                        }
                    }
                    text.startsWith(DESCRIPTION) -> {
                        val content = text.removePrefix("$DESCRIPTION:").trim()
                        if (content.isNotEmpty()) description.add(content)
                    }
                    text.startsWith(PARAMETER) || text.startsWith(ENUM) || text.startsWith(RETURN) -> description.add(it)
                    text.startsWith(DEPRECATED) -> {
                        model.isDeprecated = true
                        description.add(it)
                    }
                    text.startsWith(EXPERIMENTAL) -> {
                        model.isExperimental = true
                        description.add(it)
                    }
                    text.startsWith(TUTORIAL) -> {
                        val groups = TUTORIAL_REGEX.find(it.trimStart())?.groups
                        val url = groups?.get(2)?.value
                        if (url != null) {
                            model.tutorials.add(GdTutorial().apply {
                                this.url = url
                                name = groups[1]?.value ?: url
                            })
                        }
                        description.add(it)
                    }
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

    @NlsSafe
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

                    if (!descriptions.containsKey(prefix)) prefix = DESCRIPTION
                    else text = text.substringAfter(" ")
                    if (prefix != TUTORIAL) {
                        text = GdBBCodeRenderer.renderToHtml(text)
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

}
