package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.psi.*

class GdRootContributor : CompletionContributor() {

    val AFTER_INHERITANCE = psiElement().withParent(
        psiElement(PsiErrorElement::class.java).afterSibling(psiElement(GdTypes.INHERITANCE))
    );
    val AFTER_CLASS_NAMING = psiElement().withParent(
        psiElement(PsiErrorElement::class.java).afterSibling(psiElement(GdTypes.CLASS_NAMING))
    );
    val AFTER_TOOL = psiElement().withParent(
        psiElement(PsiErrorElement::class.java).afterSibling(psiElement(GdTypes.TOOLLINE))
    );
    //val IN_INHERITANCE: ElementPattern<PsiElement> = psiElement().afterLeaf(INHERITANCE)

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;

        if (!psiElement().withParents(PsiErrorElement::class.java, GdFile::class.java).accepts(position)) {
            return;
        }

        val previous = PsiTreeUtil.prevCodeLeaf(position.originalElement);

        if (previous === null) {
            result.addElement(GdLookup.text(GdKeywords.EXTENDS, " "));
        } else if (AFTER_INHERITANCE.accepts(position)) {
            result.addElement(GdLookup.text(GdKeywords.CLASS_NAME, " "));
            result.addElement(GdLookup.text(GdKeywords.TOOL, "\n"));
            result.addElement(GdLookup.text(GdKeywords.FUNC, " "));
        } else if (AFTER_CLASS_NAMING.accepts(position)) {
            result.addElement(GdLookup.text(GdKeywords.TOOL, "\n"));
            result.addElement(GdLookup.text(GdKeywords.FUNC, " "));
        } else if (AFTER_TOOL.accepts(position)) {
            result.addElement(GdLookup.text(GdKeywords.FUNC, " "));
        }

        result.stopHere();
        return;
    }

}
