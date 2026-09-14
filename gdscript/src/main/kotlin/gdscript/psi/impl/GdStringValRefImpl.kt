package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdStringValRef
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdStringNameMemberReferenceUtil
import org.jetbrains.annotations.Unmodifiable

class GdStringValRefImpl(node: ASTNode) : GdRefElementImpl(node), GdStringValRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitStringVal(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    /**
     * Signal / method names inside `connect("sig", ...)`, `call("m", ...)`, `Callable(self, "m")`
     * and similar StringName parameters - see [GdStringNameMemberReferenceUtil]. Classic `res://`
     * resource references stay on [com.intellij.psi.PsiReference] via
     * [gdscript.reference.GdResourceReferenceContributor].
     */
    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> =
        GdStringNameMemberReferenceUtil.getOwnReferences(this)
}
