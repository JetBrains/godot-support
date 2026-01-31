package gdscript.codeInsight

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import gdscript.GdLexerAdapter
import gdscript.GdScriptBundle
import gdscript.highlighter.GdTokenTypeSet
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumValueNmi
import gdscript.psi.GdForSt
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdNamedElement
import gdscript.psi.GdSignalIdNmi
import gdscript.psi.GdStringValRef
import gdscript.psi.GdVarNmi

class GdUsageProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            GdLexerAdapter(),
            GdTokenTypeSet.IDENTIFIERS,
            GdTokenTypeSet.COMMENT,
            TokenSet.EMPTY,
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is GdClassNameNmi
                || psiElement is GdMethodIdNmi
                || psiElement is GdEnumDeclNmi
                || psiElement is GdEnumValueNmi
                || psiElement is GdSignalIdNmi
                || psiElement is GdVarNmi
                || psiElement is GdForSt
                || psiElement is GdStringValRef
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getType(element: PsiElement): String {
        return when(element) {
            is GdClassNameNmi -> GdScriptBundle.message("find.usages.classes")
            is GdMethodIdNmi -> GdScriptBundle.message("find.usages.methods")
            is GdEnumDeclNmi -> GdScriptBundle.message("find.usages.enums")
            is GdEnumValueNmi -> GdScriptBundle.message("find.usages.enum.consts")
            is GdSignalIdNmi -> GdScriptBundle.message("find.usages.signals")
            is GdVarNmi -> GdScriptBundle.message("find.usages.variables")
            is GdStringValRef -> GdScriptBundle.message("find.usages.resources")
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when(element) {
            is GdNamedElement -> element.name.orEmpty()
            else -> element.text
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when(element) {
            is GdNamedElement -> element.name.orEmpty()
            else -> element.text
        }
    }

}
