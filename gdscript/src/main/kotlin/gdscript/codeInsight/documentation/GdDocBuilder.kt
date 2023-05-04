package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.VirtualFileUtil.localParentPath

class GdDocBuilder(val project: Project) {

    private var owner: HtmlChunk? = null
    private var packaged: HtmlChunk? = null
    private var preview: HtmlChunk? = null
    private var bodyBlocks: MutableList<HtmlChunk> = mutableListOf()

    /**
     * @param element GdClassDecl|GdFile from getOwningClassElement
     */
    fun withOwner(element: PsiElement?): GdDocBuilder {
        if (element == null) return this
        val body = GdDocUtil.iconed("AllIcons.Nodes.Class")
        val link = GdClassUtil.getFullClassId(element)
        body.add(GdDocUtil.elementLink(link))
        owner = HtmlChunk.div().children(body)
        return this
    }

    fun addBodyBlock(vararg chunks: HtmlChunk): GdDocBuilder {
        this.bodyBlocks.addAll(chunks)
        return this
    }

    fun withPackage(element: PsiElement): GdDocBuilder {
        return withPackage(element.containingFile.virtualFile.localParentPath())
    }

    fun withPackage(path: String): GdDocBuilder {
        val body = GdDocUtil.iconed("AllIcons.Nodes.Package")

        var currentPath = ""
        path.split("/").forEachIndexed { index, s ->
            currentPath += "/$s"
            if (index == 0) {
                currentPath = currentPath.trimStart('/')
            } else {
                body.add(HtmlChunk.text("/"))
            }

            body.add(GdDocUtil.packageLink(currentPath.trimStart('/'), s))
        }
        packaged = HtmlChunk.div().children(body)
        return this
    }

    fun withPreview(code: String?): GdDocBuilder {
        if (code == null) return this
        preview = HtmlChunk.raw(code)
        return this
    }

    override fun toString(): String {
        val sb = StringBuilder()
        line(sb, owner)
        code(sb, preview)
        bodyBlocks.forEach { sb.append(it) }
        line(sb, packaged)

        return sb.toString()
    }

    private fun line(sb: StringBuilder, element: Any?) {
        if (element == null) return
        sb.append(DocumentationMarkup.CONTENT_START)
        sb.append(element)
        sb.append(DocumentationMarkup.CONTENT_END)
    }

    private fun code(sb: StringBuilder, element: Any?) {
        if (element == null) return
        sb.append(HtmlChunk.tag("pre").children(
                GdDocCode.createHighlightedSnippet(element.toString(), project),
        ).wrapWith(DocumentationMarkup.DEFINITION_ELEMENT))
    }

}
