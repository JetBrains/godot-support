package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.utils.GdClassCompletionUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdNamedElement

/**
 * ReturnType reference to classId
 */
class GdTypeHintReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.text;
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName);
        return element;
    }

    override fun resolve(): PsiElement? {
        return GdClassNamingIndex.getGlobally(key, element).firstOrNull()?.classNameNmi;
    }

    override fun getVariants(): Array<LookupElement> {
        return GdClassCompletionUtil.allRootClasses(element.project);
    }

}
