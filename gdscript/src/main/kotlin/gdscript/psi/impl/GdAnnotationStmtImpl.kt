package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAnnotationParams
import gdscript.psi.GdAnnotationType
import gdscript.psi.GdStmt
import gdscript.psi.GdVisitor

class GdAnnotationStmtImpl : ASTWrapperPsiElement, GdStmt {

    constructor(node: ASTNode): super(node)

    fun accept(visitor: GdVisitor) {
        visitor.visitAnnotationSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor) else super.accept(visitor)
    }

    fun getAnnotationParams(): GdAnnotationParams? {
        return PsiTreeUtil.getChildOfType(this, GdAnnotationParams::class.java)
    }

    fun getAnnotationType(): GdAnnotationType {
        return notNullChild(PsiTreeUtil.getChildOfType(this, GdAnnotationType::class.java))
    }

}
