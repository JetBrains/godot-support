package gdscript.codeInsight

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import gdscript.GdLexerAdapter
import gdscript.GdScriptBundle
import gdscript.highlighter.GdTokenTypeSet
import gdscript.polySymbols.psi.gdPsiPolySymbolsFor
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumValueNmi
import gdscript.psi.GdForSt
import gdscript.psi.GdKeyNmi
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

    /**
     * A GDScript declaration that backs a PolySymbol is searched through that symbol, so this
     * provider does not claim it.
     *
     * The platform builds a classic PSI usage target from this method - see
     * [com.intellij.find.findUsages.DefaultUsageTargetProvider]. A symbol that is a
     * [com.intellij.find.usages.api.SearchTarget] adds a second target of its own, and Find Usages
     * then asks which of the two to search. Both find the same usages, because
     * [com.intellij.polySymbols.search.PolySymbolUsageSearcher] reports every PolySymbol reference,
     * so the symbol target alone is enough. It also works when the caret sits on a usage, which the
     * PSI target does not, because a GDScript usage site holds no classic
     * [com.intellij.psi.PsiReference] - see [gdscript.search.GdOwnReferencesSearcher].
     */
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        if(psiElement.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY) != null) {
            return false
        }

        if (gdPsiPolySymbolsFor(psiElement).isNotEmpty()) {
            return false
        }

        return psiElement is GdClassNameNmi
                || psiElement is GdMethodIdNmi
                || psiElement is GdEnumDeclNmi
                || psiElement is GdEnumValueNmi
                || psiElement is GdSignalIdNmi
                || psiElement is GdVarNmi
                || psiElement is GdForSt
                || psiElement is GdStringValRef
                || psiElement is GdKeyNmi
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
            is GdKeyNmi -> GdScriptBundle.message("find.usages.dictionary.keys")
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when(element) {
            is GdNamedElement -> element.name ?: ""
            else -> element.text
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when(element) {
            is GdNamedElement -> element.name ?: ""
            else -> element.text
        }
    }

}
