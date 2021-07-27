package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns.psiElement
import gdscript.GdKeywords
import gdscript.completion.util.GdClassCompletionUtil
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance

class GdClassNameCompletionContributor : CompletionContributor() {

    val CLASS_NAME_NM = psiElement().afterLeaf(psiElement().withText(GdKeywords.CLASS_NAME))
        .withSuperParent(2, GdClassNaming::class.java);

    val INHERITANCE_ID = psiElement().afterLeaf(psiElement().withText(GdKeywords.EXTENDS))
        .withSuperParent(2, GdInheritance::class.java);

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (CLASS_NAME_NM.accepts(parameters.position)) {
            val filename = parameters.originalFile.name;
            result.addElement(
                GdLookup.create(filename.substring(0, filename.length - 3))
            );
            result.stopHere();
            return;
        } else if (INHERITANCE_ID.accepts(parameters.position)) {
            GdClassCompletionUtil.statics(result);
        }
    }

}
