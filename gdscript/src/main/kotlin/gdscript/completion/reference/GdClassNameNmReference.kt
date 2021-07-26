package gdscript.completion.reference

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.containers.toArray
import gdscript.psi.utils.PsiGdClassNamingUtil

class GdClassNameNmReference : PsiReferenceBase<PsiElement>, PsiPolyVariantReference {
    // TODO
    private var key: String = "";

    constructor(element: PsiElement, textRange: TextRange) : super(element, textRange) {
        key = element.text.substring(textRange.startOffset, textRange.endOffset)
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult?> {
        val project: Project = myElement.project;
        val classNames = PsiGdClassNamingUtil.listClassNameNm(project);

        return classNames.map {
            PsiElementResolveResult(it)
        }.toArray(arrayOfNulls(classNames.size));
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)

        return if (resolveResults.size == 1) resolveResults[0]!!.element else null
    }

    override fun getVariants(): Array<Any?> {
        val project = myElement.project;
        val classNames = PsiGdClassNamingUtil.listClassNameNm(project);

        return classNames.map {
            LookupElementBuilder
                .create(it.toString())
                .withTypeText("Losos")
        }.toArray(arrayOfNulls(classNames.size));
    }

}
