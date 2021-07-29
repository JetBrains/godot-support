package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.util.GdClassVarCompletionUtil
import gdscript.psi.GdFile
import gdscript.psi.GdTypes

class GdRootContributor : CompletionContributor() {

    val AFTER_INHERITANCE =
        psiElement().withParent(psiElement(PsiErrorElement::class.java).afterSiblingSkipping(psiElement(PsiWhiteSpace::class.java),
            psiElement(GdTypes.INHERITANCE))
        );
    val AFTER_CLASS_NAMING =
        psiElement().withParent(psiElement(PsiErrorElement::class.java).afterSiblingSkipping(psiElement(PsiWhiteSpace::class.java),
            psiElement(GdTypes.CLASS_NAMING))
        );
    val AFTER_TOOL =
        psiElement().withParent(psiElement(PsiErrorElement::class.java).afterSiblingSkipping(psiElement(PsiWhiteSpace::class.java),
            psiElement(GdTypes.TOOLLINE))
        );
    val AFTER_CONST =
        psiElement().withParent(psiElement(PsiErrorElement::class.java).afterSiblingSkipping(psiElement(PsiWhiteSpace::class.java),
            psiElement(GdTypes.CONST_DECL_TL))
        );
    val AFTER_VAR =
        psiElement().withParent(psiElement(PsiErrorElement::class.java).afterSiblingSkipping(psiElement(PsiWhiteSpace::class.java),
            psiElement(GdTypes.CLASS_VAR_DECL_TL))
        );

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;

        if (!psiElement().withParents(PsiErrorElement::class.java, GdFile::class.java).accepts(position)) {
            return;
        }

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
        }

        result.stopHere();
        return;
    }

    private fun addTopLvlDecl(result: CompletionResultSet) {
        result.addElement(GdLookup.create(GdKeywords.FUNC, " "));
        result.addElement(GdLookup.create(GdKeywords.CONST, " "));
        result.addElement(GdLookup.create(GdKeywords.VAR, " "));
        GdClassVarCompletionUtil.annotations(result);
    }

}
