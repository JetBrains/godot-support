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
        return psiElement is GdClassNameNmi
                || psiElement is GdMethodIdNmi
                || psiElement is GdEnumDeclNmi
                || psiElement is GdEnumValueNmi
                || psiElement is GdSignalIdNmi
                || psiElement is GdVarNmi
                || psiElement is GdForSt
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null;
    }

    override fun getType(element: PsiElement): String {
        return when(element) {
            is GdClassNameNmi -> "classes"
            is GdMethodIdNmi -> "methods"
            is GdEnumDeclNmi -> "enums"
            is GdEnumValueNmi -> "enum consts"
            is GdSignalIdNmi -> "signals"
            is GdVarNmi -> "variables"
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
