package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdDictPattern
import gdscript.psi.GdKeyValuePattern
import gdscript.psi.GdVisitor

class GdDictPatternImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdDictPattern {
    fun accept(visitor: GdVisitor) {
        visitor.visitDictPattern(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val keyValuePatternList: List<GdKeyValuePattern>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdKeyValuePattern::class.java)
}
