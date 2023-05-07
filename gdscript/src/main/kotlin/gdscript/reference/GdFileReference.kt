package gdscript.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile

class GdFileReference : PsiReferenceBase<GdFile> {

    constructor(element: GdFile): super(element, true)

    override fun handleElementRename(newElementName: String): PsiElement {
        element.name = newElementName
        return element
    }

    override fun resolve(): PsiElement? {
        return PsiTreeUtil.getStubChildOfType(element, GdClassNaming::class.java)?.classNameNmi
    }

}
