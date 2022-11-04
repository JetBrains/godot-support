package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.utils.GdClassCompletionUtil
import gdscript.completion.utils.GdFileCompletionUtil
import gdscript.index.impl.GdClassIdIndex
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdInheritanceIdNm
import gdscript.psi.GdInheritanceSubIdNm
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdClassUtil

/**
 * Inheritance reference to classId
 */
class GdInheritanceNmReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.parent.text.substring(0, element.textRangeInParent.endOffset);
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO rename resource -> rename file
        if (isResource()) return element;

        when (element) {
            is GdInheritanceIdNm -> element.setName(newElementName);
            is GdInheritanceSubIdNm -> element.setName(newElementName);
        }

        return element;
    }

    override fun resolve(): PsiElement? {
        return GdClassIdIndex.getGloballyResolved(key, element.project).firstOrNull();
    }

    override fun getVariants(): Array<LookupElement> {
        if (isResource()) { // At Top level "res://Asd.gd" can be used
            return GdFileCompletionUtil.listFileResources(element.project, true, true);
        } else if (element is GdInheritanceSubIdNm) { // While at nested position, only InnerClasses
            val classId = GdClassIdIndex.getGloballyResolved(key.substring(0, key.lastIndexOf(".")), element.project)
                .firstOrNull() ?: return emptyArray();
            val container = GdClassUtil.getOwningClassElement(classId);
            val inners = PsiTreeUtil.getStubChildrenOfTypeAsList(container, GdClassDeclTl::class.java);

            return GdClassCompletionUtil.classDecl(*inners.toTypedArray());
        }

        return GdClassCompletionUtil.allRootClasses(element.project);
    }

    private fun isResource(): Boolean {
        return key.startsWith('"') && key.endsWith('"');
    }

}
