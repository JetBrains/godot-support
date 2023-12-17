package gdscript.symbol

import com.intellij.model.Symbol
import com.intellij.psi.PsiElement

class GdPsiSymbol : Symbol {

    lateinit var myElement: PsiElement
    lateinit var myPointer: GdSymbolPointer

    constructor(element: PsiElement) {
        GdPsiSymbol(element, GdSymbolPointer(element))
    }

    constructor(element: PsiElement, pointer: GdSymbolPointer) {
        myElement = element
        myPointer = pointer
    }

    fun getElement(): PsiElement {
        return myElement
    }

    override fun createPointer(): GdSymbolPointer {
        return myPointer
    }

}
