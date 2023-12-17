package gdscript.symbol

import com.intellij.model.Pointer
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager

class GdSymbolPointer : Pointer<GdPsiSymbol?> {
    private val myPointer: Pointer<out PsiElement>
    private val myElement: PsiElement

    constructor(element: PsiElement) {
        this.myElement = element
        myPointer = SmartPointerManager.createPointer(myElement)
    }

    override fun dereference(): GdPsiSymbol? {
        val element = myPointer.dereference()
        return if (element == null) {
            null
        } else {
            GdPsiSymbol(element, this)
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val pointer = o as GdSymbolPointer
        return if (myPointer != pointer.myPointer) false else true
    }

    override fun hashCode(): Int {
        return myPointer.hashCode()
    }

}
