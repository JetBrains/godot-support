package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.stubs.IStubElementType
import gdscript.index.stub.GdClassIdStub
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdPsiUtils.getClassId
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getNameIdentifier
import gdscript.psi.GdPsiUtils.getParentName
import gdscript.psi.GdPsiUtils.isInner
import gdscript.psi.GdPsiUtils.setName
import gdscript.psi.GdVisitor

class GdClassNameNmiImpl : GdClassIdElementImpl, GdClassNameNmi {
    constructor(node: ASTNode) : super(node)

    constructor(stub: GdClassIdStub, type: IStubElementType<*, *>) : super(stub, type)

    fun accept(visitor: GdVisitor) {
        visitor.visitClassNameNmi(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val classId: String
        get() = getClassId(this)

    override val parentName: String?
        get() = getParentName(this)

    override val isInner: Boolean
        get() = isInner(this)

    override fun setName(newName: String): PsiElement =
        setName(this, newName)

    override fun getName(): String =
        getName(this)

    override fun getNameIdentifier(): PsiElement =
        getNameIdentifier(this)
}
