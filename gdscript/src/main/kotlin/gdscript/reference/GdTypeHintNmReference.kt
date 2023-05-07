package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.utils.GdClassCompletionUtil
import gdscript.completion.utils.GdClassCompletionUtil.lookup
import gdscript.completion.utils.GdEnumCompletionUtil.lookup
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdClassUtil

/**
 * ReturnType reference to ClassId & EnumDecl
 */
class GdTypeHintNmReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = ""

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.parent.text.substring(0, element.textRangeInParent.endOffset)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName)
        return element
    }

    override fun resolve(): PsiElement? {
        val classId = GdClassNamingIndex.INSTANCE.getGlobally(key, element).firstOrNull()?.classNameNmi
        if (classId != null) return classId

        val container = if (key.contains(".")) {
            val ownerClassId = getOwnerClass() ?: return null
            GdClassUtil.getOwningClassElement(ownerClassId)
        } else {
            GdClassUtil.getOwningClassElement(element)
        }

        val myName = element.name
        enums(container).forEach {
            if (it.name == myName) return it.enumDeclNmi
        }
        innerClasses(container).forEach {
            if (it.name == myName) return it.classNameNmi
        }

        return null
    }

    override fun getVariants(): Array<LookupElement> {
        val variants = mutableListOf<LookupElement>()
        val container: PsiElement?

        if (key.contains(".")) {
            val classId = getOwnerClass() ?: return emptyArray()
            container = GdClassUtil.getOwningClassElement(classId)

            variants.addAll(innerClasses(container).map { it.lookup() })
        } else {
            container = GdClassUtil.getOwningClassElement(element)
            variants.addAll(GdClassCompletionUtil.allRootClasses(element.project))
        }

        variants.addAll(enums(container).mapNotNull { it.lookup() })

        return variants.toTypedArray()
    }

    private fun getOwnerClass(): PsiElement? {
        val withoutLast = key.substring(0, key.lastIndexOf("."))
        val global = GdClassUtil.getClassIdElement(withoutLast, element)
        if (global != null) return global

        val myId = GdClassUtil.getFullClassId(element)
        return GdClassUtil.getClassIdElement("${myId}.${withoutLast}", element)
    }

    private fun enums(ownerClass: PsiElement): List<GdEnumDeclTl> {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(ownerClass, GdEnumDeclTl::class.java)
    }

    private fun innerClasses(ownerClass: PsiElement): List<GdClassDeclTl> {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(ownerClass, GdClassDeclTl::class.java)
    }

}
