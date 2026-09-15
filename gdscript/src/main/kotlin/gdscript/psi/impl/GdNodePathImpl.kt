package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistryImpl
import gdscript.psi.GdNodePath
import gdscript.psi.GdVisitor

class GdNodePathImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdNodePath {
    fun accept(visitor: GdVisitor) {
        visitor.visitNodePath(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistryImpl.getReferencesFromProviders(this)
    }
}
