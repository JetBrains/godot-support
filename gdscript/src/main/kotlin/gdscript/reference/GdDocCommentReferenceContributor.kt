package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import gdscript.codeInsight.documentation.GdDocLinkResolver

/**
 * `## See [Node] and [method Node.add_child].`
 */
class GdDocCommentReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(register: PsiReferenceRegistrar) {
        register.registerReferenceProvider(
            psiElement(PsiComment::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<PsiReference> {
                    return GdDocCommentReference.collect(element as PsiComment)
                }
            }
        )
    }

}

class GdDocCommentReference(
    element: PsiComment,
    range: TextRange,
    private val link: String,
) : PsiReferenceBase<PsiComment>(element, range, true) {

    override fun resolve(): PsiElement? = GdDocLinkResolver.resolve(null, link, element)

    companion object {
        private val reference = "\\[(?:(member|constant|method|enum|signal|param|annotation) )?([A-Za-z_@][\\w@.]*)]".toRegex()
        private val inlineCode = "\\[code].*?\\[/code]".toRegex()
        private val tags = setOf("b", "i", "u", "s", "br", "lb", "rb", "code", "codeblock", "codeblocks", "center", "kbd", "url", "gdscript", "csharp")

        fun collect(comment: PsiComment): Array<PsiReference> {
            val text = comment.text
            if (!text.startsWith("##")) return EMPTY_ARRAY
            val codeRanges = inlineCode.findAll(text).map { it.range }.toList()
            return reference.findAll(text)
                .filter { match -> codeRanges.none { match.range.first in it } }
                .mapNotNull { match ->
                    val kind = match.groups[1]?.value
                    val name = match.groups[2]!!
                    if (kind == null && name.value in tags) return@mapNotNull null
                    val link = if (kind == null) name.value else "$kind:${name.value}"
                    GdDocCommentReference(comment, TextRange(name.range.first, name.range.last + 1), link)
                }
                .toList()
                .toTypedArray<PsiReference>()
        }
    }

}
