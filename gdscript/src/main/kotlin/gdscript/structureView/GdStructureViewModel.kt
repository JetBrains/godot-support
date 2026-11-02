package gdscript.structureView

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMethodDeclTl

class GdStructureViewModel(psiFile: PsiFile, editor: Editor?) :
    StructureViewModelBase(psiFile, editor, GdStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    init {
        withSuitableClasses(*GdStructureViewElement.MEMBER_CLASSES)
    }

    override fun getSorters(): Array<Sorter> {
        return arrayOf(Sorter.ALPHA_SORTER)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        return false
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        val value = element.value
        return value is GdClassVarDeclTl
            || value is GdConstDeclTl
            || value is GdMethodDeclTl
            || value is GdEnumDeclTl
    }
}
