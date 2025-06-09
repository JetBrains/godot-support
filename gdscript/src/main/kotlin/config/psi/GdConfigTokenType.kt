package config.psi

import com.intellij.psi.tree.IElementType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import org.jetbrains.annotations.NonNls

class GdConfigTokenType(debugName: @NonNls String) :
    IElementType(debugName, GdLanguage) {

    override fun toString(): String {
        return "GdConfigTokenType." + super.toString()
    }

}
