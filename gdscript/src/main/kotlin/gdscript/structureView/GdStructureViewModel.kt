package gdscript.structureView

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMethodDeclTl

class GdStructureViewModel : StructureViewModelBase, StructureViewModel.ElementInfoProvider {

    constructor(psiFile: PsiFile) : super(psiFile, GdStructureViewElement(psiFile))

    override fun getSorters(): Array<Sorter> {
        return arrayOf(Sorter.ALPHA_SORTER);
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        return false;
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return element is GdClassVarDeclTl
                || element is GdConstDeclTl
                || element is GdMethodDeclTl
                || element is GdEnumDeclTl
    }

}
