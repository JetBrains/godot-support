package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.psi.GdClassNameNmi

class GdClassNameReference : PsiReferenceBase<GdClassNameNmi> {

    constructor(element: GdClassNameNmi): super(element, TextRange(0, element.textLength))

    override fun resolve(): PsiElement? {
        ElementManipulators.INSTANCE
        return element.containingFile
    }

}
