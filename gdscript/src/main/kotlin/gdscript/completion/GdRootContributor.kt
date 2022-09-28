package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.util.*
import gdscript.psi.*
import gdscript.psi.utils.PsiGdMethodDeclUtil
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class GdRootContributor : CompletionContributor() {

    companion object {
        val ROOT_POSITION = psiElement(GdTypes.IDENTIFIER)
            .withParents(
                PsiErrorElement::class.java,
                GdFile::class.java,
            )
        val ANNOTATOR_DECL = psiElement(GdTypes.ANNOTATOR)
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
        val previous = PsiTreeUtil.prevCodeLeaf(position.originalElement);

        if (previous === null) {
            result.addElement(GdLookup.create(GdKeywords.EXTENDS, " "));
            return result.stopHere();
        } else if (ROOT_POSITION.accepts(position)) {
            addTopLvlDecl(result, parameters);
        } else if (ANNOTATOR_DECL.accepts(position)) {
            GdClassVarCompletionUtil.annotations(result, false);
            return result.stopHere();
        } else if (psiElement().withParent(psiElement(GdTypes.REF_ID_NM)).accepts(position)) {
            // TODO tohle by nemělo být za tečkou
            addRefKeywords(result);
        }
    }

    private fun addTopLvlDecl(result: CompletionResultSet, parameters: CompletionParameters) {
        val list = PsiGdMethodDeclUtil.collectParentsMethods(parameters.position.containingFile);
        GdMethodCompletionUtil.addMethods(list, result, true);
        result.addElement(GdLookup.create(GdKeywords.FUNC, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.STATIC, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.CONST, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.VAR, " ", priority = GdLookup.KEYWORDS));
        GdResourceCompletionUtil.fullVarResources(parameters.position.originalElement, result);
        val filename = parameters.originalFile.name;
        result.addElement(GdLookup.create(GdKeywords.CLASS_NAME, " ${filename.substring(0, filename.length - 3)}"));
        GdClassVarCompletionUtil.annotations(result);
    }

    private fun addRefKeywords(result: CompletionResultSet) {
        result.addElement(GdLookup.create(GdKeywords.VAR, " ", priority = GdLookup.KEYWORDS));
        result.addElement(GdLookup.create(GdKeywords.SELF, priority = GdLookup.KEYWORDS));
    }

}
