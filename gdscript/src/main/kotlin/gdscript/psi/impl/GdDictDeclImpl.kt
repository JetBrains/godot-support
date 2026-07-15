package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdDictDecl
import gdscript.psi.GdKeyValue
import gdscript.psi.GdNewLineEnd
import gdscript.psi.GdVisitor

class GdDictDeclImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdDictDecl {
    fun accept(visitor: GdVisitor) {
        visitor.visitDictDecl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val keyValueList: List<GdKeyValue>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdKeyValue::class.java)

    override val newLineEndList: List<GdNewLineEnd>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdNewLineEnd::class.java)
}
