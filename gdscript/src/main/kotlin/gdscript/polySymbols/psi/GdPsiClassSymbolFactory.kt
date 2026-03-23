package gdscript.polySymbols.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdClassSymbol
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile

object GdPsiClassSymbolFactory {
    fun create(source: PsiElement): GdClassSymbol? {
        return when (source) {
            is GdClassNameNmi -> GdPsiClassSymbol(source)
            is GdClassDeclTl -> source.classNameNmi?.let { GdPsiClassSymbol(it) }
            is GdClassNaming -> source.classNameNmi?.let { GdPsiClassSymbol(it) }
            is GdFile -> {
                val namedClass = PsiTreeUtil.getStubChildOfType(source, GdClassNaming::class.java)?.classNameNmi
                namedClass?.let { GdPsiClassSymbol(it) } ?: GdPsiResourceClassSymbol(source)
            }

            else -> null
        }
    }
}
