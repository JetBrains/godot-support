package gdscript.symbol

import com.intellij.model.Symbol
import com.intellij.psi.PsiElement

class GdPsiSymbol : Symbol {

    private val myElement: PsiElement
    private val myPointer: GdSymbolPointer

    constructor(element: PsiElement) : this(element, GdSymbolPointer(element))

    constructor(element: PsiElement, pointer: GdSymbolPointer) {
        myElement = element
        myPointer = pointer
    }

    override fun createPointer(): GdSymbolPointer {
        return myPointer
    }

}
