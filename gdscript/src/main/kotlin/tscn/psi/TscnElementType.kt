package tscn.psi

import com.intellij.psi.tree.IElementType
import tscn.TscnLanguage

class TscnElementType : IElementType {

    constructor(debugName: String) : super(debugName, TscnLanguage);

}
