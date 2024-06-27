package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.utils.GdMethodCompletionUtil.lookup
import gdscript.psi.GdElementFactory
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdSetMethodIdRef
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.methods

class GdSetGetMethodIdReference : PsiReferenceBase<PsiElement> {

    private var key: String = ""

    constructor(element: PsiElement) : super(element, TextRange(0, element.textLength)) {
        key = element.text
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        when (myElement) {
            is GdGetMethodIdRef -> myElement.replace(GdElementFactory.getMethodIdRef(myElement.project, newElementName))
            is GdSetMethodIdRef -> myElement.replace(GdElementFactory.setMethodIdRef(myElement.project, newElementName))
        }

        return myElement
    }

    override fun resolve(): PsiElement? {
        val members = GdClassMemberUtil.listClassMemberDeclarations(element, false)

        return members.methods().find { it.name == key }?.methodIdNmi
    }

    override fun getVariants(): Array<Any> {
        val members = GdClassMemberUtil.listClassMemberDeclarations(element, false)

        return members.methods().map { it.lookup() }.toTypedArray()
    }

}
