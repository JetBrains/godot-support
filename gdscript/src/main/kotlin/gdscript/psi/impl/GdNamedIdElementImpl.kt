package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistryImpl
import gdscript.psi.GdNamedIdElement
import org.jetbrains.annotations.NotNull

abstract class GdNamedIdElementImpl(node: @NotNull ASTNode) : ASTWrapperPsiElement(node), GdNamedIdElement {

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistryImpl.getReferencesFromProviders(this);
    }

}
