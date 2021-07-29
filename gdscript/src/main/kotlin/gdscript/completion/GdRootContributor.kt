package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.util.GdClassVarCompletionUtil
import gdscript.completion.util.GdCompletionUtil
import gdscript.psi.*

class GdRootContributor : CompletionContributor() {

    val AFTER_INHERITANCE = psiElement()
        .withParent(
            psiElement(PsiErrorElement::class.java)
                .withParent(GdInheritance::class.java)
                .afterSibling(psiElement(GdNewLineEnd::class.java)))
    val AFTER_CLASS_NAMING = psiElement()
        .withParent(
            psiElement(PsiErrorElement::class.java)
                .withParent(GdClassNaming::class.java)
                .afterSibling(psiElement(GdNewLineEnd::class.java)))
    val AFTER_TOOL = psiElement()
        .withParent(
            psiElement(PsiErrorElement::class.java)
                .withParent(GdToolline::class.java)
                .afterSibling(psiElement(GdNewLineEnd::class.java)))
    val AFTER_CONST = psiElement()
        .withParent(psiElement(PsiErrorElement::class.java)
            .withParent(psiElement(GdConstDeclTl::class.java)
                .withLastChildSkipping(GdCompletionUtil.WHITE_OR_ERROR, psiElement(GdEndStmt::class.java)))
        );
    val AFTER_VAR = psiElement()
        .withParent(psiElement(PsiErrorElement::class.java)
            .withParent(psiElement(GdClassVarDeclTl::class.java)
                .withLastChildSkipping(GdCompletionUtil.WHITE_OR_ERROR, psiElement(GdEndStmt::class.java)))
        );

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
        val previous = PsiTreeUtil.prevCodeLeaf(position.originalElement);

        if (previous === null) {
            result.addElement(GdLookup.create(GdKeywords.EXTENDS, " "));
        } else if (AFTER_INHERITANCE.accepts(position)) {
            result.addElement(GdLookup.create(GdKeywords.CLASS_NAME, " "));
            result.addElement(GdLookup.create(GdKeywords.TOOL, "\n"));
            addTopLvlDecl(result);
        } else if (AFTER_CLASS_NAMING.accepts(position)) {
            result.addElement(GdLookup.create(GdKeywords.TOOL, "\n"));
            addTopLvlDecl(result);
        } else if (AFTER_TOOL.accepts(position)
            || AFTER_CONST.accepts(position)
            || AFTER_VAR.accepts(position)
        ) {
            addTopLvlDecl(result);
        } else {
            return;
        }

        result.stopHere();
    }

    private fun addTopLvlDecl(result: CompletionResultSet) {
        result.addElement(GdLookup.create(GdKeywords.FUNC, " "));
        result.addElement(GdLookup.create(GdKeywords.CONST, " "));
        result.addElement(GdLookup.create(GdKeywords.VAR, " "));
        GdClassVarCompletionUtil.annotations(result);
    }

}
