package tscn.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import gdscript.psi.utils.GdClassUtil

class TscnScriptClassReference : PsiReferenceBase<PsiNamedElement> {

    constructor(element: PsiNamedElement) : super(element, quotedContentRange(element.text))

    override fun resolve(): PsiElement? {
        val cache = ResolveCache.getInstance(element.project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ ->
                val className = element.text.trim('"')
                // TODO use poly symbols
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

    companion object {
        // script_class values are always quoted strings; excluding the quotes from rangeInElement keeps
        // range-based usages (e.g. PolySymbols rename's plain text-range replace) from clobbering them.
        private fun quotedContentRange(text: String): TextRange =
            if (text.length >= 2 && text.startsWith('"') && text.endsWith('"'))
                TextRange(1, text.length - 1)
            else
                TextRange(0, text.length)
    }
}
