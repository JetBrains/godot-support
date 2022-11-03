package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.utils.GdMethodCompletionUtil.lookup
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.methods

class GdSetGetMethodIdNmReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.text;
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement.setName(newElementName);

        return myElement;
    }

    override fun resolve(): PsiElement? {
        val members = GdClassMemberUtil.listClassMemberDeclarations(element, false);

        return members.methods().find { it.name == key }?.methodIdNmi;
    }

    override fun getVariants(): Array<Any> {
        val members = GdClassMemberUtil.listClassMemberDeclarations(element, false);

        return members.methods().map { it.lookup() }.toTypedArray()
    }

}
