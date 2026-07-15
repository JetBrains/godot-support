package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdStringValRef
import gdscript.psi.GdVisitor

class GdLiteralExImpl(node: ASTNode) : GdExprImpl(node), GdLiteralEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitLiteralEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val refIdNm: GdRefIdRef?
        get() = PsiTreeUtil.getChildOfType(this, GdRefIdRef::class.java)

    override val stringVal: GdStringValRef?
        get() = PsiTreeUtil.getChildOfType(this, GdStringValRef::class.java)
}
