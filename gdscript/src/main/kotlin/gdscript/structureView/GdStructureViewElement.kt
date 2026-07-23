package gdscript.structureView

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdFile
import gdscript.psi.GdMethodDeclTl

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
        element?.presentation ?: PresentationData()

    override fun getPresentableText(): String =
        element?.presentation?.presentableText ?: ""

    override fun getChildrenBase(): Collection<StructureViewTreeElement> {
        val file = element as? GdFile ?: return emptyList()
        val declarations = ArrayList<NavigatablePsiElement>()
        PsiTreeUtil.getChildrenOfTypeAsList(file, GdConstDeclTl::class.java).forEach { declarations.add(it as NavigatablePsiElement) }
        PsiTreeUtil.getChildrenOfTypeAsList(file, GdClassVarDeclTl::class.java).forEach { declarations.add(it as NavigatablePsiElement) }
        PsiTreeUtil.getChildrenOfTypeAsList(file, GdEnumDeclTl::class.java).forEach { declarations.add(it as NavigatablePsiElement) }
        PsiTreeUtil.getChildrenOfTypeAsList(file, GdMethodDeclTl::class.java).forEach { declarations.add(it as NavigatablePsiElement) }
        declarations.sortBy { it.textRange.startOffset }
        return declarations.map { GdStructureViewElement(it) }
    }

    override fun getAlphaSortKey(): String = element?.name ?: ""
}
