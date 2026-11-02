package gdscript.structureView

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdFile
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdTopLevelDecl

/**
 * Structure view node for GDScript.
 *
 * Extends [PsiTreeElementBase], which delegates region grouping to the platform's
 * `CustomRegionStructureUtil.groupByCustomRegions` (via `mergeWithExtensions`).
 * This is the same mechanism used by every other language plugin, so GDScript
 * `#region` / `#endregion` blocks — including nested and empty regions — appear
 * in the Structure View for free, as long as [gdscript.formatter.GdFoldingBuilder]
 * keeps recognising them as custom folding regions.
 */
class GdStructureViewElement(element: NavigatablePsiElement)
    : PsiTreeElementBase<NavigatablePsiElement>(element), SortableTreeElement, StructureViewTreeElement {

    override fun getPresentation(): ItemPresentation =
        element?.let { GdPresentationUtil.presentationOf(it) } ?: PresentationData()

    override fun getPresentableText(): String =
        element?.let { GdPresentationUtil.presentationOf(it) }?.presentableText ?: ""

    override fun getChildrenBase(): Collection<StructureViewTreeElement> {
        val declarations: List<GdTopLevelDecl> = when (val element = element) {
            is GdFile -> PsiTreeUtil.getChildrenOfTypeAsList(element, GdTopLevelDecl::class.java)
            is GdClassDeclTl -> element.topLevelDeclList
            else -> return emptyList()
        }
        return declarations
            .filter { declaration -> MEMBER_CLASSES.any { it.isInstance(declaration) } }
            .filterIsInstance<NavigatablePsiElement>()
            .sortedBy { it.textRange.startOffset }
            .map { GdStructureViewElement(it) }
    }

    override fun getAlphaSortKey(): String = element?.name ?: ""

    companion object {
        /** The declarations that the structure view shows. */
        internal val MEMBER_CLASSES: Array<Class<out GdTopLevelDecl>> = arrayOf(
            GdClassDeclTl::class.java,
            GdConstDeclTl::class.java,
            GdClassVarDeclTl::class.java,
            GdEnumDeclTl::class.java,
            GdMethodDeclTl::class.java,
        )
    }
}
