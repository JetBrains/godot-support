package gdscript.codeInsight

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.diagnostic.fileLogger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import gdscript.codeInsight.documentation.GdAnnotationAnchors
import gdscript.codeInsight.documentation.GdDocFactory
import gdscript.codeInsight.documentation.GdDocLinkResolver
import gdscript.codeInsight.documentation.GdDocUtil
import gdscript.codeInsight.documentation.GdGodotDocUtil
import gdscript.codeInsight.documentation.GdVirtualDocComment
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.psi.GdInheritance
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdCommentUtil
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdProjectSettingsState
import java.util.function.Consumer

private val LOG = fileLogger()

// todo: delay creating GdDocFactory after isGodotProject is evaluated
class GdDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (isLspProvider(element.project)) return null
        if (GdAnnotationAnchors.isAnchor(element)) return annotationDoc(element as PsiComment)
        return GdDocFactory.create(element, true)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (isLspProvider(element.project)) return null
        if (GdAnnotationAnchors.isAnchor(element)) return annotationDoc(element as PsiComment)
        return GdDocFactory.create(element, false)
    }

    /** Renders the signature from the annotation anchor and the `##` block above it. */
    @NlsSafe
    private fun annotationDoc(anchor: PsiComment): String {
        val signature: @NlsSafe String = anchor.text.removePrefix("#").trim()
        val docComment = (PsiTreeUtil.skipWhitespacesBackward(anchor) as? PsiComment)
            ?.takeIf { it.isDocComment() }
            ?.let { findDocComment(anchor.containingFile, it.textRange) }
        return buildString {
            append(HtmlChunk.text(signature).wrapWith(DocumentationMarkup.PRE_ELEMENT).wrapWith(DocumentationMarkup.DEFINITION_ELEMENT))
            val rendered: @NlsSafe String? = docComment?.let { generateRenderedDoc(it) }
            if (rendered != null) append(HtmlChunk.raw(rendered).wrapWith(DocumentationMarkup.CONTENT_ELEMENT))
        }
    }

    @NlsSafe
    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        val virtualComment = comment as? GdVirtualDocComment ?: return null
        val model = GdCommentUtil.collectComments(virtualComment.comments)

        return buildString {
            append(GdDocUtil.paragraph(model.description, comment.project))
            if (model.tutorials.isNotEmpty()) {
                append(GdDocUtil.listTable(
                    "tutorials",
                    model.tutorials.map { HtmlChunk.link(GdGodotDocUtil.expandDocsUrl(it.url), it.name) },
                ))
            }
        }.also { LOG.trace { "generateRenderedDoc: comment=${virtualComment.text}, rendered.length=${it.length}" } }
    }

    override fun findDocComment(file: PsiFile, range: TextRange): PsiDocCommentBase? {
        val found = GdCommentUtil.commentBlocks(file).firstOrNull { block ->
            TextRange.create(block.first().startOffset, block.last().endOffset).intersects(range)
        }?.let { GdVirtualDocComment(it) }
        LOG.trace { "findDocComment: file=${file.name}, range=$range, found=${found != null}" }
        return found
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        if (file !is GdFile) return
        val blocks = GdCommentUtil.commentBlocks(file)
        LOG.trace { "collectDocComments: file=${file.name}, blocks=${blocks.size}" }
        blocks.forEach { sink.accept(GdVirtualDocComment(it)) }
    }

    /**
     * Maps the first token after a `##` comment block to the element that the block documents.
     * Reader Mode in Rider looks up the documentation target at this offset to resolve the links in the rendered comment.
     * A class description follows the class header, and a plain comment such as `#region` can follow it. Its element is the file.
     * An annotation anchor in the generated `@GDScript` file is its own element.
     */
    override fun getCustomDocumentationElement(
            editor: Editor,
            file: PsiFile,
            contextElement: PsiElement?,
            targetOffset: Int,
    ): PsiElement? {
        if (file !is GdFile || contextElement == null || contextElement is PsiWhiteSpace || contextElement.isDocComment()) return null
        if (GdAnnotationAnchors.isAnchor(contextElement)) return contextElement
        var top: PsiElement = contextElement
        while (true) {
            val parent = top.parent
            if (parent == null || parent is PsiFile || parent.startOffset != top.startOffset) break
            top = parent
        }
        val comment = PsiTreeUtil.prevVisibleLeaf(top) as? PsiComment ?: return null
        if (!comment.isDocComment()) return null
        if (contextElement is PsiComment) {
            val beforeBlock = PsiTreeUtil.skipWhitespacesAndCommentsBackward(comment)
            return if (beforeBlock is GdInheritance || beforeBlock is GdClassNaming) file else null
        }
        val owner = GdVirtualDocComment(listOf(comment)).owner ?: return null
        if (owner is GdInheritance) return file
        return GdClassMemberUtil.identifierOf(owner)
    }

    private fun PsiElement.isDocComment(): Boolean = this is PsiComment && text.startsWith("##")

    private fun isLspProvider(project: Project): Boolean {
        return GdProjectSettingsState.getInstance(project).state.docProvider == GdDocProviderMode.LSP
    }

    override fun getDocumentationElementForLink(
            psiManager: PsiManager?,
            link: String?,
            context: PsiElement?,
    ): PsiElement? {
        if (link.isNullOrBlank() || context == null) return null
        return GdDocLinkResolver.resolve(psiManager, link, context)
    }

}
