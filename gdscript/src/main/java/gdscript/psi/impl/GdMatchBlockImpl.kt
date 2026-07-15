package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdMatchBlock
import gdscript.psi.GdPatternList
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor

class GdMatchBlockImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdMatchBlock {
    fun accept(visitor: GdVisitor) {
        visitor.visitMatchBlock(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val patternList: GdPatternList
        get() = notNullChild(
            PsiTreeUtil.getChildOfType(
                this,
                GdPatternList::class.java
            )
        )

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)
}
