package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAnnotationParams
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdAnnotationType
import gdscript.psi.GdEndStmt
import gdscript.psi.GdVisitor

class GdAnnotationTlImpl(node: ASTNode) : GdTopLevelDeclImpl(node), GdAnnotationTl {
    override fun accept(visitor: GdVisitor) {
        visitor.visitAnnotationTl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val annotationParams: GdAnnotationParams?
        get() = PsiTreeUtil.getChildOfType(this, GdAnnotationParams::class.java)

    override val annotationType: GdAnnotationType
        get() = notNullChild(
            PsiTreeUtil.getChildOfType(
                this,
                GdAnnotationType::class.java
            )
        )

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)
}
