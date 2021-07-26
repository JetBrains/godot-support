package gdscript.completion.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords

class GdRootContributor : CompletionContributor() {

    val INHERITANCE: ElementPattern<PsiElement> = psiElement().withText(GdKeywords.EXTENDS)
    val IN_INHERITANCE: ElementPattern<PsiElement> = psiElement().afterLeaf(INHERITANCE)

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;

        if (IN_INHERITANCE.accepts(position)) {
            result.addElement(GdLookup.text("asd"))
            result.stopHere();
            return;
        } else if (PsiTreeUtil.prevVisibleLeaf(position.originalElement) === null) { // TODO could not get correct pattern working
            result.addElement(
                GdLookup.text("extends", " ")
            )
        }

        result.stopHere();
        return;
    }

}
