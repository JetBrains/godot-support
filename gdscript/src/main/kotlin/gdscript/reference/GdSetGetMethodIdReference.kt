package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.utils.GdMethodCompletionUtil.lookup
import gdscript.psi.GdClassVarDeclTl
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
        val members = GdClassMemberUtil.listClassMemberDeclarations(element, staticFilter())

        return members.methods().find { it.name == key }?.methodIdNmi
    }

    override fun getVariants(): Array<Any> {
        val members = GdClassMemberUtil.listClassMemberDeclarations(element, staticFilter())

        return members.methods().map { it.lookup() }.toTypedArray()
    }

    // A static variable should only access static methods, others shouldn't be filtered
    private fun staticFilter(): Boolean? {
        val owningVar = PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassVarDeclTl::class.java)
        return if (owningVar?.isStatic == true) true else null
    }
}
