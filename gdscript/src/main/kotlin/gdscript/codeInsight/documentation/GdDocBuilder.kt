package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.VirtualFileUtil.localParentPath

class GdDocBuilder {

    private var project: Project? = null
    private var owner: HtmlChunk? = null
    private var packaged: HtmlChunk? = null
    private var preview: String? = null
    private var bodyBlocks: MutableList<HtmlChunk> = mutableListOf()

    constructor()

    constructor(project: Project) {
        this.project = project
    }

    constructor(element: PsiElement) {
        this.project = element.project
    }

    /**
     * @param element GdClassDecl|GdFile from getOwningClassElement
     */
    fun withOwner(element: PsiElement?): GdDocBuilder {
        if (element == null) return this
        val body = GdDocUtil.iconed("AllIcons.Nodes.Class")
        val link = GdClassUtil.getFullClassId(element)
        body.add(GdDocUtil.elementLink(link, GdClassUtil.getOwningClassName(element)))
        owner = HtmlChunk.div().children(body)
        return this
    }

    fun addBodyBlock(vararg chunks: HtmlChunk): GdDocBuilder {
        this.bodyBlocks.addAll(chunks)
        return this
    }

    fun withPackage(element: PsiElement): GdDocBuilder {
        if (element is PsiFile) return withPackage(element.virtualFile.localParentPath())
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
            body.add(GdDocUtil.packageLink(currentPath, s))
        }
        packaged = HtmlChunk.div().children(body)
        return this
    }

    fun withPreview(code: String?): GdDocBuilder {
        if (code == null) return this
        preview = code
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
        if (element == null || project == null) return
        sb.append(HtmlChunk.tag("pre").children(
                GdDocCode.createHighlightedSnippet(element.toString(), project!!),
        ).wrapWith(DocumentationMarkup.DEFINITION_ELEMENT))
    }

}
