package gdscript.psi

import com.intellij.psi.tree.IElementType
import gdscript.GdLanguage
import org.jetbrains.annotations.NonNls

class GdTokenType(debugName: @NonNls String) :
    IElementType(debugName, GdLanguage.INSTANCE) {

    override fun toString(): String {
        return "GdTokenType." + super.toString()
    }

}