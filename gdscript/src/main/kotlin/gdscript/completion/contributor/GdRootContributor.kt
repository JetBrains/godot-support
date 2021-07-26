package gdscript.completion.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.patterns.TreeElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.psi.*
import gdscript.psi.impl.GdClassNameNmImpl
import gdscript.psi.impl.GdClassNamingImpl

class GdRootContributor : CompletionContributor() {

    val INHERITANCE: ElementPattern<PsiElement> = psiElement().withText(GdKeywords.EXTENDS)
    val IN_INHERITANCE: ElementPattern<PsiElement> = psiElement().afterLeaf(INHERITANCE)
    val CLASS_NAME: ElementPattern<PsiElement> = psiElement().withText(GdKeywords.CLASS_NAME)

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position;
/*
  private static final ElementPattern<PsiElement> UNEXPECTED_REFERENCE_AFTER_DOT = or(
      // dot at the statement beginning
      psiElement().afterLeaf(".").insideStarting(psiExpressionStatement()),
      // like `call(Cls::methodRef.<caret>`
      psiElement().afterLeaf(psiElement(JavaTokenType.DOT).afterSibling(psiElement(PsiMethodCallExpression.class).withLastChild(
        psiElement(PsiExpressionList.class).withLastChild(psiElement(PsiErrorElement.class))))));


      if (psiElement().insideStarting(psiElement(PsiLocalVariable.class, PsiExpressionStatement.class)).accepts(myPosition)) {
 */
        val bfg = PsiTreeUtil.prevCodeLeaf(position.originalElement);

        if (psiElement().insideStarting(psiElement(GdClassNaming::class.java, GdClassNameNmImpl::class.java, GdClassNamingImpl::class.java, GdClassNameNm::class.java)).accepts(position)) {
            result.addElement(GdLookup.text("class_name"));
        }


        if (psiElement().afterLeaf(IN_INHERITANCE).accepts(position)) {
            result.addElement(GdLookup.text("asd"));
        }
        if (psiElement().afterLeaf(psiElement().withParent(GdInheritance::class.java)).accepts(position)) {
            result.addElement(GdLookup.text("asd"));
        }
        if (psiElement().afterLeaf(psiElement(GdInheritanceIdNm::class.java).withParent(GdInheritance::class.java)).accepts(position)) {
            result.addElement(GdLookup.text("asd"));
        }
        if (PlatformPatterns.psiElement(GdTypes.CLASS_NAME).withSuperParent(3, GdClassNamingImpl::class.java).accepts(position)) {
            result.addElement(GdLookup.text("asd"));
        }
        if (psiElement(GdTypes.CLASS_NAME).accepts(position)) {
            result.addElement(GdLookup.text("asd"));
        }
        if (psiElement(GdTypes.CLASS_NAME).withParent(psiElement(GdTypes.CLASS_NAMING)).accepts(position)) {
            result.addElement(GdLookup.text("asd"));
        }
        if (psiElement().insideStarting(psiElement(GdTypes.CLASS_NAMING)).accepts(position)) {
            result.addElement(GdLookup.text("class_name"));
        }

        if (IN_INHERITANCE.accepts(position)) {
            // TODO
            result.addElement(GdLookup.text("asd"))
        } else if (CLASS_NAME.accepts(position)) {
            result.addElement(GdLookup.text(GdKeywords.CLASS_NAME, " "))
        } else if (PsiTreeUtil.prevCodeLeaf(position.originalElement) === null) { // TODO could not get correct pattern working
            result.addElement(GdLookup.text(GdKeywords.EXTENDS, " "))
        }

        result.stopHere();
        return;
    }

}
