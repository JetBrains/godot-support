package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.stub.GdSignalDeclStub
import gdscript.model.GdTutorial
import gdscript.psi.GdEndStmt
import gdscript.psi.GdParamList
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getParameters
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdSignalIdNmi
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdCommentUtil.brief
import gdscript.psi.utils.GdCommentUtil.description
import gdscript.psi.utils.GdCommentUtil.isDeprecated
import gdscript.psi.utils.GdCommentUtil.isExperimental
import gdscript.psi.utils.GdCommentUtil.tutorials

class GdSignalDeclTlImpl : GdSignalDeclElementImpl, GdSignalDeclTl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: GdSignalDeclStub, type: IStubElementType<*, *>) : super(stub, type)

    fun accept(visitor: GdVisitor) {
        visitor.visitSignalDeclTl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)

    override val paramList: GdParamList?
        get() = PsiTreeUtil.getChildOfType(this, GdParamList::class.java)

    override val signalIdNmi: GdSignalIdNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdSignalIdNmi::class.java)

    override fun getName(): String =
        getName(this)

    override val parameters: LinkedHashMap<String, String>
        get() = getParameters(this)

    override fun description(): String =
        description(this)

    override fun brief(): String =
        brief(this)

    override fun tutorials(): List<GdTutorial> =
        tutorials(this)

    override fun isDeprecated(): Boolean =
        isDeprecated(this)

    override fun isExperimental(): Boolean =
        isExperimental(this)
}
