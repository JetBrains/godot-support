package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdEndStmt
import gdscript.psi.GdSetDecl
import gdscript.psi.GdSetMethodIdRef
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdTyped
import gdscript.psi.GdVarNmi
import gdscript.psi.GdVisitor

class GdSetDeclImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdSetDecl {
    fun accept(visitor: GdVisitor) {
        visitor.visitSetDecl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)

    override val setMethodIdNm: GdSetMethodIdRef?
        get() = PsiTreeUtil.getChildOfType(this, GdSetMethodIdRef::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)

    override val typed: GdTyped?
        get() = PsiTreeUtil.getChildOfType(this, GdTyped::class.java)

    override val varNmi: GdVarNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdVarNmi::class.java)
}
