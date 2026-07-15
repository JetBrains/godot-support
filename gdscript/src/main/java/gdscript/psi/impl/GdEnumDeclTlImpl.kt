package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.stub.GdEnumDeclStub
import gdscript.model.GdTutorial
import gdscript.psi.GdEndStmt
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getPresentation
import gdscript.psi.GdPsiUtils.getValues
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdCommentUtil.brief
import gdscript.psi.utils.GdCommentUtil.description
import gdscript.psi.utils.GdCommentUtil.isDeprecated
import gdscript.psi.utils.GdCommentUtil.isExperimental
import gdscript.psi.utils.GdCommentUtil.tutorials

class GdEnumDeclTlImpl : GdEnumDeclElementImpl, GdEnumDeclTl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: GdEnumDeclStub, type: IStubElementType<*, *>) : super(stub, type)

    fun accept(visitor: GdVisitor) {
        visitor.visitEnumDeclTl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)

    override val enumDeclNmi: GdEnumDeclNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdEnumDeclNmi::class.java)

    override val enumValueList: List<GdEnumValue>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdEnumValue::class.java)

    override fun getName(): String =
        getName(this)

    override val values: LinkedHashMap<String, Long>
        get() = getValues(this)

    override fun getPresentation(): ItemPresentation =
        getPresentation(this)

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
