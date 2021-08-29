package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdNamedElement

class GdSetGetMethodIdNmReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement, textRange: TextRange) : super(element as GdNamedElement, textRange) {
        key = element.text.substring(textRange.startOffset, textRange.endOffset)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement.setName(newElementName);

        return myElement;
    }

    override fun resolve(): PsiElement? {
        return GdMethodDeclIndex
            .get(key, myElement.project, GlobalSearchScope.fileScope(myElement.containingFile))
            .firstOrNull()?.methodIdNmi;
    }

    override fun getVariants(): Array<Any> {
        val methods =
            PsiTreeUtil.findChildrenOfType(myElement.containingFile.originalElement, GdMethodDeclTl::class.java);

        return methods.mapNotNull {
            it.name?.let { it1 -> GdLookup.create(it1, icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER)) }
        }.toTypedArray()
    }

}
