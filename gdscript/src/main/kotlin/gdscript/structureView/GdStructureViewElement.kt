package gdscript.structureView

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*

class GdStructureViewElement : StructureViewTreeElement, SortableTreeElement {

    private var myElement: NavigatablePsiElement;

    constructor(element: NavigatablePsiElement) {
        myElement = element
    }

    override fun getPresentation(): ItemPresentation = myElement.presentation ?: PresentationData();

    override fun getChildren(): Array<TreeElement> {
        val elements = mutableListOf<TreeElement>()
        if (myElement is GdFile) {
            val consts = PsiTreeUtil.getChildrenOfTypeAsList(myElement, GdConstDeclTl::class.java);
            elements.addAll(consts.map {
                GdStructureViewElement(it as NavigatablePsiElement)
            })

            val variables = PsiTreeUtil.getChildrenOfTypeAsList(myElement, GdClassVarDeclTl::class.java);
            elements.addAll(variables.map {
                GdStructureViewElement(it as NavigatablePsiElement)
            })

            val methods = PsiTreeUtil.getChildrenOfTypeAsList(myElement, GdMethodDeclTl::class.java);
            elements.addAll(methods.map {
                GdStructureViewElement(it as NavigatablePsiElement)
            })
        }

        return elements.toTypedArray();
    }

    override fun navigate(requestFocus: Boolean) {
        myElement.navigate(requestFocus);
    }

    override fun canNavigate(): Boolean = myElement.canNavigate();

    override fun canNavigateToSource(): Boolean = myElement.canNavigateToSource();

    override fun getValue(): Any = myElement;

    override fun getAlphaSortKey(): String = myElement.name ?: ""

}
