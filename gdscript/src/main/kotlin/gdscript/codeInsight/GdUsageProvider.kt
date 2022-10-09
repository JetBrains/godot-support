package gdscript.codeInsight

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import gdscript.GdLexerAdapter
import gdscript.highlighter.GdTokenTypeSet
import gdscript.psi.*

class GdUsageProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            GdLexerAdapter(),
            GdTokenTypeSet.IDENTIFIERS,
            GdTokenTypeSet.COMMENT,
            TokenSet.EMPTY,
        );
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is GdClassNameNm
                || psiElement is GdMethodIdNmi
                || psiElement is GdClassVarIdNmi
                || psiElement is GdConstIdNmi
                || psiElement is GdEnumDeclNmi
                || psiElement is GdEnumValueNmi
                || psiElement is GdSignalIdNmi
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null;
    }

    override fun getType(element: PsiElement): String {
        return when(element) {
            is GdClassNameNm -> "class"
            is GdMethodIdNmi -> "method"
            is GdClassVarIdNmi -> "variable"
            is GdConstIdNmi -> "constant"
            is GdEnumDeclNmi -> "enum"
            is GdEnumValueNmi -> "enum const"
            is GdSignalIdNmi -> "signal"
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when(element) {
            is GdNamedElement -> element.name.orEmpty()
            else -> ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when(element) {
            is GdNamedElement -> element.name.orEmpty()
            else -> ""
        }
    }

}
