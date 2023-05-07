package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.GdLookup
import gdscript.completion.utils.GdClassCompletionUtil
import gdscript.completion.utils.GdClassCompletionUtil.lookup
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdInheritanceSubIdNm
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdClassUtil

/**
 * Inheritance reference to classId
 */
class GdInheritanceNmReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.parent.text.substring(0, element.textRangeInParent.endOffset)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO rename resource -> rename file
        if (isResource()) return element

        element.setName(newElementName)
        return element;
    }

    override fun resolve(): PsiElement? {
        return GdClassUtil.getClassIdElement(key, element)
    }

    override fun getVariants(): Array<LookupElement> {
        if (isResource()) { // At Top level "res://Asd.gd" can be used
            return GdFileResIndex.INSTANCE.getNonEmptyKeys(element).mapNotNull {
                if (it.endsWith(".gd")) GdLookup.create("\"$it", lookup = "\"")
                else null
            }.toTypedArray()
        } else if (element is GdInheritanceSubIdNm) { // While at nested position, only InnerClasses
            val classId = GdClassUtil.getClassIdElement(key.substring(0, key.lastIndexOf(".")), element)
                ?: return emptyArray()
            val container = GdClassUtil.getOwningClassElement(classId)
            val inners = PsiTreeUtil.getStubChildrenOfTypeAsList(container, GdClassDeclTl::class.java)

            return inners.map { it.lookup() }.toTypedArray();
        }

        return GdClassCompletionUtil.allRootClasses(element.project)
    }

    private fun isResource(): Boolean {
        return key.startsWith('"') && key.endsWith('"')
    }

}
