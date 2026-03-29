package tscn.psi

import com.intellij.psi.tree.IElementType
import com.jetbrains.rider.godot.community.tscn.TscnLanguage

class TscnElementType : IElementType {

    constructor(debugName: String) : super(debugName, TscnLanguage)

}
