package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.stub.GdInheritanceStub
import gdscript.psi.GdEndStmt
import gdscript.psi.GdInheritance
import gdscript.psi.GdInheritanceId
import gdscript.psi.GdPsiUtils.getInheritancePath
import gdscript.psi.GdVisitor

class GdInheritanceImpl : GdInheritanceElementImpl, GdInheritance {
    constructor(node: ASTNode) : super(node)

    constructor(stub: GdInheritanceStub, type: IStubElementType<*, *>) : super(stub, type)

    fun accept(visitor: GdVisitor) {
        visitor.visitInheritance(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)

    override val inheritanceId: GdInheritanceId?
        get() = PsiTreeUtil.getChildOfType(this, GdInheritanceId::class.java)

    override val inheritancePath: String
        get() = getInheritancePath(this)
}
