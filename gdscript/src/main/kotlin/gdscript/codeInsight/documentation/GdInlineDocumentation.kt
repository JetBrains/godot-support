package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.diagnostic.fileLogger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.InlineDocumentation
import com.intellij.platform.backend.documentation.InlineDocumentationProvider
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import gdscript.codeInsight.GdDocumentationTarget
import gdscript.psi.GdFile
import gdscript.psi.utils.GdCommentUtil

private val LOG = fileLogger()

/** A `##` comment block that Reader Mode and the gutter "Toggle Rendered View" action render in place. */
class GdInlineDocumentation(val comment: GdVirtualDocComment) : InlineDocumentation {

    override fun getDocumentationRange(): TextRange = comment.textRange

    override fun getDocumentationOwnerRange(): TextRange? = comment.owner?.textRange

    override fun renderText(): String = renderGdDocComment(comment)

    override fun getOwnerTarget(): DocumentationTarget? = comment.owner?.let { GdDocumentationTarget(it) }
}

class GdInlineDocumentationProvider : InlineDocumentationProvider {

    override fun inlineDocumentationItems(file: PsiFile): Collection<InlineDocumentation> {
        if (file !is GdFile) return emptyList()
        val blocks = GdCommentUtil.commentBlocks(file)
        LOG.trace { "inlineDocumentationItems: file=${file.name}, blocks=${blocks.size}" }
        return blocks.map { GdInlineDocumentation(GdVirtualDocComment(it)) }
    }

    override fun findInlineDocumentation(file: PsiFile, textRange: TextRange): InlineDocumentation? =
        findGdDocComment(file, textRange)?.let { GdInlineDocumentation(it) }
}

/** Finds the `##` comment block that intersects [range]. */
fun findGdDocComment(file: PsiFile, range: TextRange): GdVirtualDocComment? {
    if (file !is GdFile) return null
    val found = GdCommentUtil.commentBlocks(file).firstOrNull { block ->
        TextRange.create(block.first().startOffset, block.last().endOffset).intersects(range)
    }?.let { GdVirtualDocComment(it) }
    LOG.trace { "findGdDocComment: file=${file.name}, range=$range, found=${found != null}" }
    return found
}

/** Renders the description and the tutorials of a `##` comment block. */
@NlsSafe
fun renderGdDocComment(comment: GdVirtualDocComment): String {
    val model = GdCommentUtil.collectComments(comment.comments)
    return buildString {
        append(GdDocHtml.paragraph(model.description, comment.project))
        if (model.tutorials.isNotEmpty()) {
            append(GdDocHtml.listTable(
                "tutorials",
                model.tutorials.map { HtmlChunk.link(GdBBCodeRenderer.expandDocsUrl(it.url), it.name) },
            ))
        }
    }.also { LOG.trace { "renderGdDocComment: comment=${comment.text}, rendered.length=${it.length}" } }
}

/** Renders the signature from the annotation anchor and the `##` block above it. */
@NlsSafe
fun renderGdAnnotationDoc(anchor: PsiComment): String {
    val signature: @NlsSafe String = anchor.text.removePrefix("#").trim()
    val docComment = (PsiTreeUtil.skipWhitespacesBackward(anchor) as? PsiComment)
        ?.takeIf { it.text.startsWith("##") }
        ?.let { findGdDocComment(anchor.containingFile, it.textRange) }
    return buildString {
        append(HtmlChunk.text(signature).wrapWith(DocumentationMarkup.PRE_ELEMENT).wrapWith(DocumentationMarkup.DEFINITION_ELEMENT))
        if (docComment != null) append(HtmlChunk.raw(renderGdDocComment(docComment)).wrapWith(DocumentationMarkup.CONTENT_ELEMENT))
    }
}
