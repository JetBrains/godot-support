package tscn.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistryImpl
import org.jetbrains.annotations.NotNull
import tscn.psi.TscnNamedElement

abstract class TscnNamedElementImpl(node: @NotNull ASTNode) : ASTWrapperPsiElement(node), TscnNamedElement {

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistryImpl.getReferencesFromProviders(this)
    }

}
