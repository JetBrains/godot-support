package tscn.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import gdscript.psi.utils.GdClassUtil
import tscn.psi.TscnElementFactory

class TscnScriptClassReference : PsiReferenceBase<PsiNamedElement> {

    constructor(element: PsiNamedElement) : super(element, TextRange(0, element.textLength))

    override fun resolve(): PsiElement? {
        val cache = ResolveCache.getInstance(element.project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ ->
                val className = element.text.trim('"')
                val classIdElement = GdClassUtil.getClassIdElement(className, element, element.project)
                classIdElement
            },
            false,
            false,
        )
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName)
        return element
    }
}
