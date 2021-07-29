package gdscript

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
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
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null;
    }

    override fun getType(element: PsiElement): String {
        return if (element is GdClassNameNm) {
            "class";
        } else {
            "";
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return if (element is GdClassNameNm) {
            element.name.orEmpty();
        } else {
            "";
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return if (element is GdClassNameNm) {
            element.name.orEmpty();
        } else {
            "";
        }
    }

}
