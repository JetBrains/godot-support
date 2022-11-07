package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.utils.*
import gdscript.completion.utils.GdMethodCompletionUtil.lookupDeclaration
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.methods
import gdscript.psi.utils.GdNodeUtil
import gdscript.psi.utils.PsiGdFileUtil

/**
 * Root & InnerClass root level completions
 * keywords, functions (+ overrides), annotations, $NodePaths
 */
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

        val TO_HINT_KEYWORDS = arrayOf(
            GdKeywords.FUNC,
            GdKeywords.STATIC,
            GdKeywords.CONST,
            GdKeywords.VAR,
            GdKeywords.CLASS,
            GdKeywords.EXTENDS,
        )
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
        val previous = PsiTreeUtil.prevCodeLeaf(position.originalElement);

        if (previous === null) {
            // First text in a file
            result.addElement(GdLookup.create(GdKeywords.EXTENDS, " "));
            result.addElement(GdLookup.create("@tool"));
            addClassName(parameters, result);
        } else if (ROOT_POSITION.accepts(position)) {
            // Main class scope
            addClassName(parameters, result);
            addTopLvlDecl(parameters, result);
        } else if (INNER_CLASS_POSITION.accepts(position)) {
            // Inner class
            addTopLvlDecl(parameters, result);
        } else if (ANNOTATOR_DECL.accepts(position)) {
            // After "@"
            GdClassVarCompletionUtil.annotations(result, false);
        }
    }

    private fun addClassName(parameters: CompletionParameters, result: CompletionResultSet) {
        val className = PsiGdFileUtil.filename(parameters.position.containingFile);
        result.addElement(GdLookup.create(GdKeywords.CLASS_NAME, " $className"));
    }

    private fun addTopLvlDecl(parameters: CompletionParameters, result: CompletionResultSet) {
        GdNodeUtil.listNodes(parameters.position).forEach { result.addElement(it.variable_lookup()) }
        result.addAllElements(TO_HINT_KEYWORDS.map { GdLookup.create(it, " ", priority = GdLookup.KEYWORDS) })
        GdClassVarCompletionUtil.annotations(result);

        val members = mutableListOf<PsiElement>();
        GdClassMemberUtil.collectFromParents(parameters.position, members, false);
        result.addAllElements(members.methods().map { it.lookupDeclaration() });
    }

}
