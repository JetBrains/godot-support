package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolDeclaration

abstract class GdClassNameNmElementImpl(node: ASTNode) : GdNamedElementImpl(node) {

    override fun getOwnDeclarations(): MutableIterable<PsiSymbolDeclaration> {
        return super.getOwnDeclarations()
    }

}