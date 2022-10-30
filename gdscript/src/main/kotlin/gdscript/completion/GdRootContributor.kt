package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.utils.*
import gdscript.psi.*
import gdscript.psi.utils.PsiGdMethodDeclUtil

class GdRootContributor : CompletionContributor() {

    companion object {
        val ROOT_POSITION = psiElement(GdTypes.IDENTIFIER)
            .withParents(
                PsiErrorElement::class.java,
                GdFile::class.java,
            )
        val INNER_CLASS_POSITION = psiElement(GdTypes.IDENTIFIER)
            .withParents(
                GdFile::class.java,
            )
        val ANNOTATOR_DECL = psiElement(GdTypes.ANNOTATOR)
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
        val previous = PsiTreeUtil.prevCodeLeaf(position.originalElement);

        if (previous === null) { // First text in a file
            result.addElement(GdLookup.create(GdKeywords.EXTENDS, " "));
            result.addElement(GdLookup.create(GdKeywords.CLASS_NAME, " "));
            return result.stopHere();
        } else if (ROOT_POSITION.accepts(position)) { // Class scope
            addTopLvlDecl(result, parameters);
        } else if (INNER_CLASS_POSITION.accepts(position)) { // Inner class
            addTopLvlDecl(result, parameters, false);
        } else if (ANNOTATOR_DECL.accepts(position)) { // After "@"
            GdClassVarCompletionUtil.annotations(result, false);
            return result.stopHere();
        }
    }

    private fun addTopLvlDecl(result: CompletionResultSet, parameters: CompletionParameters, withClassName: Boolean = true) {
        val list = PsiGdMethodDeclUtil.collectParentsMethods(parameters.position.containingFile);
        GdMethodCompletionUtil.addMethods(list, result, true);
        result.addElement(GdLookup.create(GdKeywords.FUNC, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.STATIC, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.CONST, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.VAR, " ", priority = GdLookup.KEYWORDS));
        GdResourceCompletionUtil.fullVarResources(parameters.position.originalElement, result);
        if (withClassName) {
            val filename = parameters.originalFile.name;
            result.addElement(GdLookup.create(GdKeywords.CLASS_NAME, " ${filename.substring(0, filename.length - 3)}"));
        }
        result.addElement(GdLookup.create(GdKeywords.CLASS, " "));
        result.addElement(GdLookup.create(GdKeywords.EXTENDS, " "));
        GdClassVarCompletionUtil.annotations(result);
    }

}
