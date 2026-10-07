package gdscript.codeInsight

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.model.Pointer
import com.intellij.openapi.util.NlsSafe
import com.intellij.platform.backend.documentation.DocumentationLinkHandler
import com.intellij.platform.backend.documentation.DocumentationResult
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.DocumentationTargetProvider
import com.intellij.platform.backend.documentation.LinkResolveResult
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.startOffset
import gdscript.codeInsight.documentation.GdAnnotationAnchors
import gdscript.codeInsight.documentation.GdDocFactory
import gdscript.codeInsight.documentation.GdDocLinkResolver
import gdscript.codeInsight.documentation.GdVirtualDocComment
import gdscript.codeInsight.documentation.renderGdAnnotationDoc
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.psi.GdInheritance
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.settings.GdProjectSettingsState

/**
 * Tells if the plugin, and not the LSP server, documents [element].
 *
 * The user can select the LSP server as the documentation provider.
 * A generated `.generated.gd` SDK file is an exception.
 * The plugin creates that file in memory from the SDK data, so the LSP server does not know it.
 * The plugin always documents such a file.
 */
fun documentsGdElement(element: PsiElement): Boolean =
    !GdProjectSettingsState.getInstance(element.project).usesLspDocs() || element.isInGeneratedSdkFile()

private fun PsiElement.isInGeneratedSdkFile(): Boolean =
    containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY) != null

/**
 * Generates the HTML documentation of [element].
 * Returns `null` when the LSP provides the documentation.
 */
fun generateGdDoc(element: PsiElement, fullDoc: Boolean = true): @NlsSafe String? {
    if (!documentsGdElement(element)) return null
    if (GdAnnotationAnchors.isAnchor(element)) return renderGdAnnotationDoc(element as PsiComment)
    return GdDocFactory.create(element, fullDoc)
}

class GdDocumentationTarget(private val element: PsiElement) : DocumentationTarget {

    val targetElement: PsiElement get() = element

    override fun createPointer(): Pointer<out DocumentationTarget> {
        val pointer = element.createSmartPointer()
        return Pointer { pointer.dereference()?.let { GdDocumentationTarget(it) } }
    }

    override fun computePresentation(): TargetPresentation {
        val name = (element as? PsiNamedElement)?.name ?: element.containingFile?.name ?: element.text.orEmpty()
        return TargetPresentation.builder(name)
            .icon(element.getIcon(0))
            .presentation()
    }

    override val navigatable: Navigatable? get() = element as? Navigatable

    override fun computeDocumentationHint(): String? = generateGdDoc(element, false)

    override fun computeDocumentation(): DocumentationResult? =
        generateGdDoc(element)?.let { DocumentationResult.documentation(it) }
}

class GdDocumentationTargetProvider : PsiDocumentationTargetProvider {
    override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? =
        gdDocumentationTarget(element)
}

/**
 * The documentation target of [element], or `null` when the plugin does not document it.
 *
 * A Poly Symbol must use this function instead of `createPsiDocumentationTarget`.
 * That platform function builds a target over the deprecated `DocumentationProvider` extension point,
 * which GDScript no longer implements.
 */
fun gdDocumentationTarget(element: PsiElement): DocumentationTarget? {
    if (element.containingFile !is GdFile) return null
    if (!documentsGdElement(element)) return null
    return GdDocumentationTarget(element)
}

/**
 * Maps the first token after a `##` comment block to the element that the block documents.
 * Reader Mode in Rider looks up the documentation target at this offset to resolve the links in the rendered comment.
 * A class description follows the class header, and a plain comment such as `#region` can follow it. Its element is the file.
 * An annotation anchor in the generated `@GDScript` file is its own element.
 */
class GdOffsetDocumentationTargetProvider : DocumentationTargetProvider {

    override fun documentationTargets(file: PsiFile, offset: Int): List<DocumentationTarget> {
        val element = documentedElement(file, offset) ?: return emptyList()
        return listOf(GdDocumentationTarget(element))
    }

    private fun documentedElement(file: PsiFile, offset: Int): PsiElement? {
        if (file !is GdFile) return null
        val contextElement = file.findElementAt(offset) ?: return null
        if (contextElement is PsiWhiteSpace || contextElement.isDocComment()) return null
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
}

/** Resolves the `psi_element://` links of GDScript documentation to new targets. */
class GdDocumentationLinkHandler : DocumentationLinkHandler {
    override fun resolveLink(target: DocumentationTarget, url: String): LinkResolveResult? {
        if (target !is GdDocumentationTarget) return null
        if (!url.startsWith(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL)) return null
        val link = url.removePrefix(DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL)
        val context = target.targetElement
        val resolved = GdDocLinkResolver.resolve(context.manager, link, context) ?: return null
        return LinkResolveResult.resolvedTarget(GdDocumentationTarget(resolved))
    }
}
