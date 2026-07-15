package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.stub.GdMethodDeclStub
import gdscript.model.GdTutorial
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdMethodSpecifier
import gdscript.psi.GdParamList
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getParameters
import gdscript.psi.GdPsiUtils.getPresentation
import gdscript.psi.GdPsiUtils.getReturnType
import gdscript.psi.GdPsiUtils.isConstructor
import gdscript.psi.GdPsiUtils.isStatic
import gdscript.psi.GdPsiUtils.isVariadic
import gdscript.psi.GdReturnHint
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdCommentUtil.brief
import gdscript.psi.utils.GdCommentUtil.description
import gdscript.psi.utils.GdCommentUtil.isDeprecated
import gdscript.psi.utils.GdCommentUtil.isExperimental
import gdscript.psi.utils.GdCommentUtil.tutorials

class GdMethodDeclTlImpl : GdMethodDeclElementImpl, GdMethodDeclTl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: GdMethodDeclStub, type: IStubElementType<*, *>) : super(stub, type)

    fun accept(visitor: GdVisitor) {
        visitor.visitMethodDeclTl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val methodIdNmi: GdMethodIdNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdMethodIdNmi::class.java)

    override val methodSpecifierList: List<GdMethodSpecifier>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdMethodSpecifier::class.java)

    override val paramList: GdParamList?
        get() = PsiTreeUtil.getChildOfType(this, GdParamList::class.java)

    override val returnHint: GdReturnHint?
        get() = PsiTreeUtil.getChildOfType(this, GdReturnHint::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)

    override val isStatic: Boolean
        get() = isStatic(this)

    override val isVariadic: Boolean
        get() = isVariadic(this)

    override fun getName(): String =
        getName(this)

    override val returnType: String
        get() = getReturnType(this)

    override val parameters: LinkedHashMap<String, String>
        get() = getParameters(this)

    override fun getPresentation(): ItemPresentation =
        getPresentation(this)

    override val isConstructor: Boolean
        get() = isConstructor(this)

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
