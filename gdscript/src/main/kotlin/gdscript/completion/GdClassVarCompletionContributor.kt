package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.util.GdClassVarCompletionUtil
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdTypes

class GdClassVarCompletionContributor : CompletionContributor() {

    val AFTER_ANNOTATION = psiElement().afterLeaf(psiElement(GdTypes.ANNOTATOR));
    val AFTER_TYPED = psiElement().withParent(psiElement(PsiErrorElement::class.java)
        .afterSiblingSkipping(psiElement(PsiWhiteSpace::class.java),
            PlatformPatterns.or(
                psiElement(GdTypes.TYPED),
                psiElement(GdTypes.EXPR)
            )
        )
    )
    val SET_METHOD = psiElement().withParent(psiElement(GdTypes.SET_METHOD_ID_NM))
    val GET_METHOD = psiElement().withParent(psiElement(GdTypes.GET_METHOD_ID_NM))

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (AFTER_ANNOTATION.accepts(parameters.position)) {
            result.addElement(GdLookup.create(GdKeywords.VAR, " "));
            GdClassVarCompletionUtil.annotations(result);
            result.stopHere();
        } else if (AFTER_TYPED.accepts(parameters.position)) {
            result.addElement(GdLookup.create(GdKeywords.SETGET, " "));
            result.stopHere();
        } else if (SET_METHOD.accepts(parameters.position)) {
            addMethodName("set", parameters.position, result);
        } else if (GET_METHOD.accepts(parameters.position)) {
            addMethodName("get", parameters.position, result);
        }
    }

    private fun addMethodName(prefix: String, element: PsiElement, result: CompletionResultSet) {
        val name = PsiTreeUtil.getParentOfType(element, GdClassVarDeclTl::class.java)?.varName;
        result.addElement(GdLookup.create("${prefix}_$name", priority = GdLookup.TOP));
    }

}
