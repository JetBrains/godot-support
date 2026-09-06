package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdInheritanceId
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdInheritanceSubIdRef
import gdscript.psi.GdVisitor

class GdInheritanceIdImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdInheritanceId {
    fun accept(visitor: GdVisitor) {
        visitor.visitInheritanceId(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val inheritanceIdNm: GdInheritanceIdRef
        get() = notNullChild(
            PsiTreeUtil.getChildOfType(
                this,
                GdInheritanceIdRef::class.java
            )
        )

    override val inheritanceSubIdNmList: List<GdInheritanceSubIdRef>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdInheritanceSubIdRef::class.java)
}
