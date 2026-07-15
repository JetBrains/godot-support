package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdEndStmt
import gdscript.psi.GdGetDecl
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor

class GdGetDeclImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdGetDecl {
    fun accept(visitor: GdVisitor) {
        visitor.visitGetDecl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)

    override val getMethodIdNm: GdGetMethodIdRef?
        get() = PsiTreeUtil.getChildOfType(this, GdGetMethodIdRef::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)
}
