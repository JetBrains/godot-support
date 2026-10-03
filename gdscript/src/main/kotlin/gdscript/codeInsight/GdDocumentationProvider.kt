package gdscript.codeInsight

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.diagnostic.fileLogger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.openapi.vfs.findDirectory
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import gdscript.codeInsight.documentation.GdDocFactory
import gdscript.codeInsight.documentation.GdDocUtil
import gdscript.codeInsight.documentation.GdVirtualDocComment
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdFile
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdCommentUtil
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdProjectSettingsState
import gdscript.utils.PsiElementUtil.psi
import org.jetbrains.annotations.NonNls
import java.util.function.Consumer

private val LOG = fileLogger()

// todo: delay creating GdDocFactory after isGodotProject is evaluated
class GdDocumentationProvider : AbstractDocumentationProvider() {

    companion object {
        @NonNls const val LINK_ENUM_VALUE: String = "enumValue"
        @NonNls const val LINK_PACKAGE: String = "package"
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (isLspProvider(element.project)) return null
        return GdDocFactory.create(element, true)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (isLspProvider(element.project)) return null
        return GdDocFactory.create(element, false)
    }

    @NlsSafe
    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        val virtualComment = comment as? GdVirtualDocComment ?: return null
        val model = GdCommentUtil.collectComments(virtualComment.comments)

        return buildString {
            append(GdDocUtil.paragraph(model.description, comment.project))
            if (model.tutorials.isNotEmpty()) {
                append(GdDocUtil.listTable("tutorials", model.tutorials.map { HtmlChunk.link(it.url, it.name) }))
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

    private fun isLspProvider(project: Project): Boolean {
        return GdProjectSettingsState.getInstance(project).state.docProvider == GdDocProviderMode.LSP
    }

    override fun getDocumentationElementForLink(
            psiManager: PsiManager?,
            link: String?,
            context: PsiElement?,
    ): PsiElement? {
        if (link.isNullOrBlank() || context == null) return null
        val project = context.project
        if (link.contains(":") && !link.startsWith("res://")) {
            val prefix = link.substringBefore(":")
            val subLink = link.substringAfter(":")
            if (prefix == LINK_ENUM_VALUE) {
                val enumName = subLink.substringBefore(".")
                val enumValue = subLink.substringAfter(".")
                GdClassMemberUtil.listDeclarations(context, enumName).firstOrNull()?.let {
                    if (it is GdEnumDeclTl) {
                        return it.enumValueList.find { value -> value.enumValueNmi.name == enumValue }?.enumValueNmi
                    }
                }
            } else if (prefix == LINK_PACKAGE) {
                var directory = ProjectFileIndex.getInstance(project).getContentRootForFile(project.projectFile!!) ?: return null
                if (subLink.contains("/")) directory = directory.findDirectory(subLink.substringAfter("/")) ?: return null

                return PsiManager.getInstance(project).findDirectory(directory)
            }

            return null
        }

        if (context.containingFile != null) {
            GdClassMemberUtil.listDeclarations(context, link).firstOrNull()?.psi()?.let {
                GdClassMemberUtil.identifierOf(it)?.let { identifier -> return identifier }
            }
        }
        GdClassUtil.getClassIdElement(link, context, project)?.let { return it }

        return null
    }

}
